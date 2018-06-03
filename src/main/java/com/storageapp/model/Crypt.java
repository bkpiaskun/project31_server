package com.storageapp.model;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Class used to encode/decode text to safely store it
 */
public class Crypt {
    /**
     * Hashes provided string with SHA-512
     * @throws NoSuchAlgorithmException if method cant find such algorithm
     * @param  str  String to get hashed
     * @return      String hashed string
     */
    public static String getHashCodeFromString(String str) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-512");
                md.update(str.getBytes());
        byte byteData[] = md.digest();
        StringBuffer hashCodeBuffer = new StringBuffer();
        for (int i = 0; i < byteData.length; i++) {
            hashCodeBuffer.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
        }
        return hashCodeBuffer.toString();
    }
    /**
     * Encode's or Decode's provided text based on password, it split between line's so its capable of decoding/encoding more than single words
     * @param  CryptOrDecrypt  Boolean if it should Encode or Decode provided password
     * @param  txt  String to get encoded/decoded
     * @param  pass  String Base for encoding/decoding password's
     * @return      String encoded/decoded string
     */
    public String Encode(Boolean CryptOrDecrypt, String txt, String pass) {
        try {
            pass = getHashCodeFromString(pass);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        int move = 126 - 32 + 1;
        String result = "";
        int intChar;
        int pss;
        pss = pass.length();
        String[] splited = txt.split("\n");
        for(int n=0;n<splited.length;n++)
        {
            for(int x=0;x<splited[n].length();x++)
            {
                char[] crsplit;
                crsplit = splited[n].toCharArray();

                intChar = (int)(crsplit[x]);
                pss %= move;
                if (CryptOrDecrypt)
                {
                    intChar += pss;
                }
                if(!CryptOrDecrypt)
                {
                    intChar -= pss;
                }
                if (intChar > 126 && intChar < 158)
                {
                    intChar -= move;
                }
                else if (intChar < 32 && intChar > 0)
                {
                    intChar += move;
                }
                if (CryptOrDecrypt)
                {
                    result = result+((char)intChar);//intZnak;
                }
                else
                {
                    result = result+((char)intChar);//intZnak;
                }
            }
            result = result.concat("\n");
        }
        return result;
    }
    public String Szyfruj(Boolean szyfrOrOdszyfr, String txt, String pass) {

        int przesun = 126 - 32 + 1;
        String result = "";
        int intZnak;
        int haslo;
        haslo = pass.length();
        char[] cr = txt.toCharArray();

        String[] splited = txt.split("\n");

        for(int n=0;n<splited.length;n++)
        {
            for(int x=0;x<splited[n].length();x++)
            {
                char[] crsplit;
                crsplit = splited[n].toCharArray();

                intZnak = (int)(crsplit[x]);
                haslo %= przesun;
                if (szyfrOrOdszyfr)
                {
                    intZnak += haslo;
                }
                if(!szyfrOrOdszyfr)
                {
                    intZnak -= haslo;
                }
                if (intZnak > 126 && intZnak < 158)
                {
                    intZnak -= przesun;
                }
                else if (intZnak < 32 && intZnak > 0)
                {
                    intZnak += przesun;
                }
                result = result+((char)intZnak);//intZnak;
            }
            result = result.concat("\n");
        }
        return result;
    }
}