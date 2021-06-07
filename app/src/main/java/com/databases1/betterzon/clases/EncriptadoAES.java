package com.databases1.betterzon.clases;

import java.security.MessageDigest;
import java.util.Arrays;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class EncriptadoAES {

    public static SecretKeySpec _crearClave_(String llaveUsada) {

        try {

            byte [] cadenaDeBytes = llaveUsada.getBytes("UTF-8");
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            cadenaDeBytes = md.digest(cadenaDeBytes);
            cadenaDeBytes = Arrays.copyOf(cadenaDeBytes, 16);

            return new SecretKeySpec(cadenaDeBytes, "AES");

        } catch (Exception e){
            return null;
        }

    } // crearClave()


    public static byte[] encriptar(String texto, String llave) {

        try {

            SecretKeySpec clave = _crearClave_(llave);
            Cipher c = Cipher.getInstance("AES");
            c.init(Cipher.ENCRYPT_MODE, clave);

            return c.doFinal( texto.getBytes("UTF-8") );

        } catch (Exception e) {
            return null;
        }

    } // encriptar()


    public static String decifrar(byte[] texto, String llave) {

        try {

            SecretKeySpec clave = _crearClave_(llave);
            Cipher c = Cipher.getInstance("AES");
            c.init(Cipher.DECRYPT_MODE, clave);
            byte [] cadenaDesencriptada = c.doFinal(texto);

            return new String(cadenaDesencriptada);

        } catch (Exception e) {
            return "";
        }

    } // encriptar()

} // EncriptadoAES