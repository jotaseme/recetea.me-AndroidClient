package es.upm.miw.myapplication.Models;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;

public class  UserToken implements Serializable {
    private static final long serialVersionUID = 1L;
    private String token;
    private static final String USER_TOKEN = "data/data/es.upm.miw.myapplication/files/token.txt";
    public UserToken(String token) {
        this.token = token;
    }


    public String getToken() {
        return token;
    }


    public static boolean isLogged(){
        File yourFile = new File(USER_TOKEN);
        try {
            yourFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ArrayList<UserToken> token = new ArrayList<>();
        FileInputStream fis = null;

        try {
            fis = new FileInputStream(USER_TOKEN);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        ObjectInputStream in = null;
        try {
            in = new ObjectInputStream(fis);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            while (true)
                token.add((UserToken) in.readObject());
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(token.size()==1){
            return true;
        }else{
            return false;
        }
    }

    public static void destroyToken(){
        File file = new File(USER_TOKEN);
        file.delete();
    }

    public static String getUserToken() {
        File yourFile = new File(USER_TOKEN);
        try {
            yourFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ArrayList<UserToken> userTokens = new ArrayList<>();
        FileInputStream fis = null;

        try {
            fis = new FileInputStream(USER_TOKEN);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        ObjectInputStream in = null;
        try {
            in = new ObjectInputStream(fis);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            while (true)
                userTokens.add((UserToken) in.readObject());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (userTokens.size()==1){
            return userTokens.get(0).getToken();
        }else{
            return null;
        }
    }

}