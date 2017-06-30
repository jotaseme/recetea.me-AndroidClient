package es.upm.miw.myapplication.Models;


import android.util.Base64;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import es.upm.miw.myapplication.Utils.AppendingObjectOutputStream;

public class  UserToken implements Serializable {
    private static final long serialVersionUID = 1L;
    private String token;
    private static final String USER_TOKEN = "data/data/es.upm.miw.myapplication/files/token.txt";
    public UserToken(String token) {
        token = token.substring(token.toString().lastIndexOf("=") + 1);
        token = token.substring(0,token.toString().length()-1);
        this.token = token;
    }


    public String getToken() {
        return token;
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

    public void saveUserToken(){
        try
        {
            File file;
            file = new File(USER_TOKEN);
            file.delete();
            FileOutputStream fos = new FileOutputStream(USER_TOKEN, true);

            if (file.length() == 0) {
                ObjectOutputStream out = new ObjectOutputStream(fos);
                out.writeObject(this);
                out.close();
            } else {
                ObjectOutputStream out = new AppendingObjectOutputStream(fos);
                out.writeObject(this);
                out.close();
            }
        }catch(IOException i)
        {
            i.printStackTrace();
        }
    }

    public static String decoded(String JWTEncoded) throws Exception {
        try {
            String[] split = JWTEncoded.split("\\.");
            int index = getJson(split[1]).indexOf("username") + 11;
            int lastIndex = getJson(split[1]).indexOf(",") -11;
            return getJson(split[1]).substring(index, index+lastIndex);
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    private static String getJson(String strEncoded) throws UnsupportedEncodingException{
        byte[] decodedBytes = Base64.decode(strEncoded, Base64.URL_SAFE);
        return new String(decodedBytes, "UTF-8");
    }

}