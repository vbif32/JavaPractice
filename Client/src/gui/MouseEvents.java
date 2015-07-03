package gui;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

import java.util.concurrent.TimeUnit;

/**
 * Created by Jay on 02.07.2015.
 * Êëàññ ñ îïèñàíèåì ğåàêöèé íà ñîáûòèÿ
 */
public class MouseEvents implements EventHandler<MouseEvent> {
    protected int scene;
    public Window_s window; //íà÷àëüíàÿ ñöåíà
    public MouseEvents (int number, Window_s window)
    {
        this.scene=number;
        this.window=window;
    }

    @Override
    public void handle(MouseEvent event) {
        System.out.println(event.getSceneY());
        System.out.println(event.getSource().getClass().getSimpleName());
        //ÎÊÍÎ ÂÕÎÄÀ
        if ((event.getSceneX()>=376)&&(event.getSceneX()<=426) && (event.getSource().getClass().getSimpleName().equals("Button")) && (this.scene==1)){
            System.out.println("Enter");
            window.page=1;
            //ÏĞÎÂÅĞÈÒÜ ÑÎÅÄÈÍÅÍÈÅ Ñ ÈÍÒÅĞÍÅÒÎÌ
            boolean check = true; //TRUE/FALSE ÇÀÌÅÍßÅÌ ÍÀ ÔÓÍÊÖÈŞ
            if (!check) //íåò ñîåäèíåíèÿ
            {
                window.Window(2);
            }
            else {
                //ÏĞÎÂÅĞÈÒÜ ÏĞÀÂÈËÜÍÎÑÒÜ ÏÀĞÎËß È ËÎÃÈÍÀ, ÏÎËÀÃÀŞ, ÂÅĞÍÅÒ TRUE/FALSE (check)
                check = false; //TRUE/FALSE ÇÀÌÅÍßÅÌ ÍÀ ÔÓÍÊÖÈŞ
                if (check) //åñëè âñå ââåäåíî âåğíî
                {
                    window.page = 3;
                    window.Window(0);
                } else //åñëè íåâåğíî ââåäåíû ïîëüçîâàòåëü èëè ïàğîëü
                {
                    window.Window(1);
                }
            }
        }
        else if ((event.getSceneX()>=435)&&(event.getSceneX()<=532) && (event.getSource().getClass().getSimpleName().equals("Button")) && (this.scene==1)) {
            //ÏÅĞÅÕÎÄ Ê ÎÊÍÓ ĞÅÃÈÑÒĞÀÖÈÈ
            System.out.println("Registration");
            window.page=2;
            window.Window(0);
        }
        else if ((event.getSceneX()>=385)&&(event.getSceneX()<=527)&&(event.getSource().getClass().getSimpleName().equals("TextField"))&&(this.scene==1)&&(event.getSceneY()>=240)&&(event.getSceneY()<=262)) {
            System.out.println("Login");
            //ÂÂÎÄ ËÎÃÈÍÀ
            //ÔÓÍÊÖÈß ×ÒÅÍÈß ËÎÃÈÍÀ
        }
        else if ((event.getSceneX()>=385)&&(event.getSceneX()<=527)&&(event.getSource().getClass().getSimpleName().equals("PasswordField"))&&(this.scene==1)&&(event.getSceneY()>=263)&&(event.getSceneY()<=283)) {
            System.out.println("Password");
            //ÂÂÎÄ ÏÀĞÎËß
            //ÔÓÍÊÖÈß ×ÒÅÍÈß ÏÀĞÎËß
        }

        //ÎÊÍÎ ĞÅÃÈÑÒĞÀÖÈÈ
        else if ((event.getSceneX()>=323)&&(event.getSceneX()<=462) && (event.getSource().getClass().getSimpleName().equals("Button")) && (this.scene==2)) {
            //ÇÀĞÅÃÈÑÒĞÈĞÎÂÀÒÜ
            System.out.println("Do registration");
            //ÏĞÎÂÅĞÈÒÜ ÑÎÅÄÈÍÅÍÈÅ Ñ ÈÍÒÅĞÍÅÒÎÌ
            boolean check = true; //TRUE/FALSE ÇÀÌÅÍßÅÌ ÍÀ ÔÓÍÊÖÈŞ
            if (check==false) //íåò ñîåäèíåíèÿ
            {
                window.Window(2);
            }
            else {
                //ÏĞÎÂÅĞÈÒÜ ÏĞÀÂÈËÜÍÎÑÒÜ ÂÂÎÄÀ ÄÀÍÍÛÕ
                check = true; //TRUE/FALSE ÇÀÌÅÍßÅÌ ÍÀ ÔÓÍÊÖÈŞ
                if (check) //åñëè âñå ïğàâèëüíî ââåäåíî
                {
                    //ÔÓÍÊÖÈß ÇÀÍÅÑÅÍÈß ÄÀÍÍÛÕ Â ÁÄ
                    window.Window(3); //Ïîêàçûâàåò, ÷òî çàğåãèñòğèğîâàë
                    System.out.println("Something");
                    window.page=1;
                    try {
                        TimeUnit.SECONDS.sleep(3);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    window.Window(0); //Ïåğåõîä ê îêíó âõîäà
                }
                else //íåïğàâèëüíî ââåäåíû äàííûå
                {
                    System.out.println(window.page);
                    window.Window(1);
                }
            }
        }
        else if ((event.getSceneX()>=472)&&(event.getSceneX()<=784) && (event.getSource().getClass().getSimpleName().equals("Button")) && (this.scene==2)) {
            //ÂÅĞÍÓÒÜÑß Ê ÎÊÍÓ ÂÕÎÄÀ
            System.out.println("Cancel");
            window.page=1;
            window.Window(0);
        }
        else if ((event.getSceneX()>=390)&&(event.getSceneX() <=531)&&(event.getSource().getClass().getSimpleName().equals("TextField"))&&(this.scene==2)&&(event.getSceneY()>=208)&&(event.getSceneY() <=228)) {
            System.out.println("Name");
            //ÇÀÏÈÑÜ ÈÌÅÍÈ
            window.Window(4);
        }
        else if ((event.getSceneX()>=390)&&(event.getSceneX()<=531)&&(event.getSource().getClass().getSimpleName().equals("TextField"))&&(this.scene==2)&&(event.getSceneY()>=229)&&(event.getSceneY()<=248)) {
            System.out.println("Surame");
            //ÇÀÏÈÑÜ ÔÀÌÈËÈÈ
        }
        else if ((event.getSceneX()>=390)&&(event.getSceneX() <=531)&&(event.getSource().getClass().getSimpleName().equals("TextField"))&&(this.scene==2)&&(event.getSceneY()>=252)&&(event.getSceneY() <=270)) {
            System.out.println("Group");
            //ÇÀÏÈÑÜ ÃĞÓÏÏÛ
        }
        else if ((event.getSceneX()>=390)&&(event.getSceneX() <=531)&&(event.getSource().getClass().getSimpleName().equals("TextField"))&&(this.scene==2)&&(event.getSceneY()>=273)&&(event.getSceneY() <=293)) {
            System.out.println("Create login");
            //ÇÀÏÈÑÜ ËÎÃÈÍÀ
        }
        else if ((event.getSceneX()>=390)&&(event.getSceneX() <=531)&&(event.getSource().getClass().getSimpleName().equals("PasswordField"))&&(this.scene==2)&&(event.getSceneY()>=295)&&(event.getSceneY() <=315)) {
            System.out.println("Create password");
            //ÇÀÏÈÑÜ ÏÀĞÎËß
        }
    }
}
