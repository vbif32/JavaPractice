package protocol;

import shed.*;
import shed.queryResult.*;

import java.io.*;
import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.security.DigestInputStream;
import java.security.DigestOutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Lognir on 04.07.2015.
 */
public class ClientSide {

    public static final File PATH = new File("src/java/protocol"); //Тут нужен путь до папки, куда кидать присланные файлы.

    public static QueryResult transmit(Query query, OutputStream out, InputStream in) {
        QueryResult qr = null;
        boolean correct = true;
        Queries type = query.getType();
        Field[] fields = query.getClass().getFields();
        StringBuilder msg = new StringBuilder();
        ArrayList<File> files = new ArrayList<>();
        try {
            for(Field f : fields) {
                if(!f.getType().equals(File.class))
                    msg.append(f.getType().getName()).append('-').append(f.getName()).append('=').append(f.get(query)).append(';');
                else {
                    msg.append(File.class.getName()).append('-').append(f.getName()).append('=');
                    if(f.get(query) != null) {
                        msg.append(((File)f.get(query)).getName());
                        files.add((File)f.get(query));
                    }
                    else msg.append("null");
                    msg.append(';');
                }
            }
            byte[] byteArr = msg.toString().getBytes();
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

            label1:
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
                    default:
                        out.write(23); out.flush(); continue;
                }

                Pattern p = Pattern.compile("([^-=;]+)-([^-=;]+)=([^=;]+);");
                Matcher m = p.matcher(rawData);
                int matchesNum = 0;
                while (m.find()) {
                    matchesNum++;
                    try {
                        if (m.group(1).compareTo(File.class.getName()) != 0 && m.group(3).compareTo("null") != 0) {
                            qr.getClass().getField(m.group(2)).set(qr, Class.forName(m.group(1)).getConstructor(String.class).newInstance(m.group(3)));
                        }
                        else if(m.group(3).compareTo("null") != 0) {
                            qr.getClass().getField(m.group(2)).set(qr, File.class.getConstructor(File.class, String.class).newInstance(PATH, m.group(3)));
                            files.add((File) qr.getClass().getField(m.group(2)).get(qr));
                        }
                        else qr.getClass().getField(m.group(2)).set(qr, null);
                    } catch(ReflectiveOperationException roe) {
                        out.write(23);
                        out.flush();
                        continue label1;
                    }
                }
                if(matchesNum != qr.getClass().getFields().length) {
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
            return new QueryError();
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
                    Pattern p = Pattern.compile("([^-=;]+)-([^-=;]+)=([^=;]+);");
                    Matcher m = p.matcher(rawData);
                    while(m.find()) {
                        qr.getClass().getField(m.group(2)).set(qr, Class.forName(m.group(1)).getConstructor(String.class).newInstance(m.group(3)));
                    }
                    return qr;
                }
                return new QueryError();
            }
            catch(Exception e) {
                return new QueryError();
                //Надо решить, что сообщать об ошибках.
            }
        }
        catch(FileReadingException|ServerBadDataException fre) {
            try {
                out.write(63);
                out.flush();
                return new QueryError();
            }
            catch(Exception e) {
                return new QueryError();
            }
        }
        catch(IllegalAccessException|NoSuchAlgorithmException iae) {
            iae.printStackTrace();
            return new QueryError();
        }
        finally {
            if(!correct)
                for(File f : files)
                    f.delete();
        }

        return qr;
    }
}
