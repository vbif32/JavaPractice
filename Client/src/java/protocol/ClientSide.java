package protocol;

import shed.*;
import shed.queryResult.*;
import javax.json.*;
import javax.json.stream.JsonParser;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.nio.ByteBuffer;
import java.security.DigestInputStream;
import java.security.DigestOutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

/**
 * Created by Lognir on 04.07.2015.
 */
public class ClientSide {

    public static final File PATH = new File("src/java/protocol"); //Тут нужен путь до папки, куда кидать присланные файлы.

    public static int parseJsonString(Object obj, String string, ArrayList<File> files) throws ReflectiveOperationException {
        JsonParser jp = Json.createParser(new StringReader(string));
        Field currentField = null;
        Class currentType = null;
        List currentArray = null;
        Object currentObject = obj;
        Stack<Object> objectStack = new Stack<>();
        Stack<Class> typeStack = new Stack<>();
        Stack<List> arrayStack = new Stack<>();
        int fieldsNum = 0;
        if(jp.hasNext()) jp.next(); //убираем первый start_obj

        while (jp.hasNext()) {
            JsonParser.Event event = jp.next();
            switch (event) {
                case KEY_NAME:
                    currentField = currentObject.getClass().getField(jp.getString());
                    if(currentObject.equals(obj)) fieldsNum++; break;
                case VALUE_STRING:
                    if(!currentField.getType().equals(File.class))
                        currentField.set(currentObject, jp.getString());
                    else {
                        currentField.set(currentObject, File.class.getConstructor(File.class, String.class).newInstance(PATH, jp.getString()));
                        files.add((File) currentField.get(currentObject));
                    } break;
                case VALUE_NUMBER:
                    currentField.set(currentObject, jp.getInt()); break;
                case VALUE_TRUE:
                    currentField.set(currentObject, true); break;
                case VALUE_FALSE:
                    currentField.set(currentObject, false); break;
                case VALUE_NULL:
                    currentField.set(currentObject, null); break;
                case START_ARRAY:
                    if(currentArray != null) {
                        arrayStack.push(currentArray);
                        typeStack.push(currentType);
                    }
                    currentArray = (List)currentField.getType().getConstructor().newInstance();
                    currentField.set(currentObject, currentArray);
                    currentType = (Class)((ParameterizedType)currentField.getGenericType()).getActualTypeArguments()[0]; break;
                case START_OBJECT:
                    objectStack.push(currentObject);
                    if(currentArray != null) {
                        currentObject = currentType.getConstructor().newInstance();
                        currentArray.add(currentObject);
                    } else {
                        Object tmp = currentField.getType().getConstructor().newInstance();
                        currentField.set(currentObject, tmp);
                        currentObject = tmp;
                    } break;
                case END_ARRAY:
                    if(typeStack.size() > 0) currentType = typeStack.pop();
                    if(arrayStack.size() > 0) currentArray = arrayStack.pop(); break;
                case END_OBJECT:
                    if(objectStack.size() > 0) currentObject = objectStack.pop(); break;
            }
        }
        jp.close();
        return fieldsNum;
    }

    public static JsonObjectBuilder getJasonObj(Object obj, ArrayList<File> files) throws IllegalAccessException {
        JsonObjectBuilder job = Json.createObjectBuilder();
        Field[] fields = obj.getClass().getFields();
        for(Field f : fields) {
            if(f.get(obj) == null)
                job.add(f.getName(), JsonValue.NULL);
            else if(f.getType().equals(File.class)) {
                File file = (File)f.get(obj);
                job.add(f.getName(), file.getName());
                files.add(file);
            }
            else if(List.class.isAssignableFrom(f.getType())) {
                job.add(f.getName(), getJasonArr(obj, f, files));
            }
            else {
                job.add(f.getName(), (f.get(obj)).toString());
            }
        }
        return job;
    }
    public static JsonArrayBuilder getJasonArr(Object obj, Field field, ArrayList<File> files) throws IllegalAccessException {
        JsonArrayBuilder jab = Json.createArrayBuilder();
        if(field.getType().isArray()) {
            Object[] arr = (Object[])field.get(obj);
            for (Object o : arr) {
                jab.add(getJasonObj(o, files));
            }
        }
        else if(List.class.isAssignableFrom(field.getType())) {
            List list = (List)field.get(obj);
            for(Object o : list) {
                jab.add(getJasonObj(o, files));
            }
        }
        return jab;
    }

    public static QueryResult transmit(Query query, OutputStream out, InputStream in) {
        QueryResult qr = null;
        boolean correct = true;
        Queries type = query.getType();
        ArrayList<File> files = new ArrayList<>();
        try {
            JsonObject jmsg = getJasonObj(query, files).build();
            byte[] byteArr = jmsg.toString().getBytes();
            byte[] len = ByteBuffer.allocate(4).putInt(byteArr.length).array();
            byte[] resMsg = new byte[byteArr.length+6];
            System.arraycopy(len, 0, resMsg, 2, 4);
            System.arraycopy(byteArr, 0, resMsg, 6, byteArr.length);
            resMsg[0] = 1;
            resMsg[1] = (byte)type.ordinal();

            int answer = 0;
            do {
                if(answer == -1) throw new IOException("Connection lost.");
                else if(answer == 63) throw new WrongDataException("Server denied request.");
                out.write(resMsg);
                out.flush();
            } while((answer = in.read()) != 33);

            MessageDigest md = MessageDigest.getInstance("MD5");
            if(files.size() > 0) {
                DigestInputStream fs;
                int fLen, numRead;
                byte[] resHead = new byte[6];
                resHead[0] = 1;
                resHead[1] = 31;
                answer = 0;
                for(File fl : files) {
                    if(fl.length() > Integer.MAX_VALUE) throw new FileReadingException("File is too large.");
                    fLen = (int)fl.length();
                    resMsg = new byte[fLen+22];
                    try {
                        fs = new DigestInputStream(new FileInputStream(fl), md);
                        numRead = fs.read(resMsg, 22, fLen);
                        fs.close();
                        if(numRead != fLen) throw new FileReadingException("File reading was not complete.");
                    }
                    catch(FileNotFoundException e) {
                        throw new FileReadingException("File was not found.");
                    }
                    catch(IOException iof) {
                        throw new FileReadingException("File could not be read.");
                    }
                    len = ByteBuffer.allocate(4).putInt(fLen).array();
                    System.arraycopy(len, 0, resHead, 2, 4);
                    System.arraycopy(md.digest(), 0, resMsg, 6, 16);
                    System.arraycopy(resHead, 0, resMsg, 0, 6);
                    md.reset();

                    do {
                        if(answer == -1) throw new IOException("Connection lost.");
                        else if(answer == 63) throw new WrongDataException("Server denied files.");
                        out.write(resMsg);
                        out.flush();
                    } while((answer = in.read()) != 33);
                }
                files.clear();
            }

            out.write(33);
            //*******
            //RECEIVE
            //*******

            int errCount, tryCount = 0;
            correct = false;
            int answerType, ansLen;
            byte[] answerLen = new byte[4];
            byte[] md5data = new byte[16];
            byte[] answerData;
            int bytesRead;

            while(!correct) {
                tryCount++;
                if(tryCount > 3) {
                    throw new ServerBadDataException("Bad data from server.");
                }
                errCount = 0;
                while ((answer = in.read()) != 1) {
                    if (answer == 63) throw new WrongDataException("Server denied files.");
                    if (answer == -1) throw new IOException("Connection lost.");
                    else if (errCount > 1024) {
                        throw new ServerBadDataException("Bad data from server.");
                    }
                    errCount++;
                }

                answerType = in.read();
                answer = in.read(answerLen, 0, 4);
                ansLen = ByteBuffer.wrap(answerLen).getInt();
                if(answer != 4 || ansLen < 0) {
                    out.write(23);
                    out.flush();
                    continue;
                }
                answerData = new byte[ansLen];
                answer = 0;
                while(answer != ansLen) {
                    bytesRead = in.read(answerData, answer, ansLen - answer);
                    answer += bytesRead;
                    if(bytesRead <= 0) break;
                }
                if (answer != ansLen) {
                    out.write(23);
                    out.flush();
                    continue;
                }

                String rawData = new String(answerData);
                //System.out.println(rawData);

                switch(answerType) {
                    case 0:
                        qr = new User(); break;
                    case 1:
                        qr = new QueryError(); break;
                    case 2:
                        qr = new Test(); break;
                    case 3:
                        qr = new LabResult(); break;
                    case 4:
                        qr = new Stats(); break;
                    case 5:
                        qr = new RegisterResult(); break;
                    default:
                        out.write(23); out.flush(); continue;
                }

                int fieldsNum;
                try {
                    fieldsNum = parseJsonString(qr, rawData, files);
                } catch(ReflectiveOperationException roe) {
                    out.write(23);
                    out.flush();
                    continue;
                }
                if(fieldsNum != qr.getClass().getFields().length) {
                    out.write(23);
                    out.flush();
                }
                else
                    correct = true;
            }
            out.write(33);

            int i = 0;
            DigestOutputStream fos;
            while((answer = in.read()) != 33) {
                if (answer == -1) throw new IOException("Connection lost.");
                correct = false;
                tryCount = 0;
                while(!correct) {
                    tryCount++;
                    if(tryCount > 3 || i >= files.size()) {
                        throw new ServerBadDataException("Bad data from server.");
                    }
                    if(answer != 1) {
                        errCount = 0;
                        while ((answer = in.read()) != 1) {
                            if (answer == -1) throw new IOException("Connection lost.");
                            else if (errCount > 1024) {
                                throw new ServerBadDataException("Bad data from server.");
                            }
                            errCount++;
                        }
                    }

                    answerType = in.read();
                    answer = in.read(answerLen, 0, 4);
                    ansLen = ByteBuffer.wrap(answerLen).getInt();
                    if (answer != 4 || answerType != 31 || ansLen < 0 || (answer = in.read(md5data, 0, 16)) != 16) {
                        out.write(23);
                        out.flush();
                        continue;
                    }
                    answerData = new byte[ansLen];
                    answer = 0;
                    while(answer != ansLen) {
                        bytesRead = in.read(answerData, answer, ansLen - answer);
                        answer += bytesRead;
                        if(bytesRead <= 0) break;
                    }
                    if (answer != ansLen) {
                        out.write(23);
                        out.flush();
                        continue;
                    }
                    try {
                        fos = new DigestOutputStream(new FileOutputStream(files.get(i)), md);
                        fos.write(answerData);
                        fos.flush();
                        fos.close();
                    }
                    catch(FileNotFoundException e) {
                        throw new FileReadingException("Couldn't make a file.");
                    }
                    catch(IOException iof) {
                        throw new FileReadingException("Couldn't write into the file.");
                    }
                    if(!Arrays.equals(md5data, md.digest())) {
                        out.write(23);
                        out.flush();
                        md.reset();
                        continue;
                    }
                    md.reset();
                    out.write(33);
                    correct = true;
                }
                i++;
            }
            if(i != files.size()) {
                correct = false;
                throw new ServerBadDataException("Bad data from server.");
            }
        }
        catch(IOException ioe) {
            return new QueryError(ioe.getMessage());
            //Надо решить, что сообщать об ошибках.
        }
        catch(WrongDataException wde) {
            try {
                if((in.read()) == 1 && (in.read()) == 1) {
                    byte[] answerLen = new byte[4];
                    if(in.read(answerLen, 0, 4) != 4) throw new Exception();
                    int ansLen = ByteBuffer.wrap(answerLen).getInt();
                    byte[] answerData = new byte[ansLen];
                    if(in.read(answerData, 0, ansLen) != ansLen) throw new Exception();
                    String rawData = new String(answerData);
                    qr = new QueryError();
                    parseJsonString(qr, rawData, null);
                    return qr;
                }
                return new QueryError(wde.getMessage());
            }
            catch(Exception e) {
                return new QueryError(e.getMessage());
                //Надо решить, что сообщать об ошибках.
            }
        }
        catch(FileReadingException|ServerBadDataException fre) {
            try {
                out.write(63);
                out.flush();
                return new QueryError(fre.getMessage());
            }
            catch(Exception e) {
                return new QueryError(e.getMessage());
            }
        }
        catch(IllegalAccessException|NoSuchAlgorithmException iae) {
            //iae.printStackTrace();
            return new QueryError(iae.getMessage());
        }
        finally {
            if(!correct)
                for(File f : files)
                    f.delete();
        }

        return qr;
    }
}
