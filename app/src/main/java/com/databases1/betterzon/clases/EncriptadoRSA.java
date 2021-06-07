package com.databases1.betterzon.clases;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import java.nio.ByteBuffer;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import javax.crypto.Cipher;

public class EncriptadoRSA {

//	// Crear las llaves
//	KeyPair keyPair = crearLlaves();
//
//  // Sacar la llave publica y privada
//  PublicKey publicKey = keyPair.getPublic();
//  PrivateKey privateKey = keyPair.getPrivate();
//
//  // Encriptar el texto
//  byte[] cipherTextArray = encriptar(text, publicKey);
//	String encryptedText = Base64.getEncoder().encodeToString(cipherTextArray);
//
//  // Decifra el texto
//  String decryptedText = decifrar(cipherTextArray, privateKey);

    public static KeyPair crearLlaves() {

        KeyPairGenerator keyPairGenerator;

        try {

            keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(4096);

            return keyPairGenerator.generateKeyPair();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    } // crearLlaves()


    public static byte[] encriptarTexto(String plainText, PublicKey publicKey) {

        try {

            Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWITHSHA-512ANDMGF1PADDING");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);

            return cipher.doFinal(plainText.getBytes());

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    } // encriptar()


    public static byte[] encriptarImagen(Bitmap imagen, PublicKey publicKey) {

        try {

            Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWITHSHA-512ANDMGF1PADDING");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);

            int size = imagen.getRowBytes() * imagen.getHeight();
            ByteBuffer bb = ByteBuffer.allocate(size);
            imagen.copyPixelsToBuffer(bb);
            byte[] byteArray = bb.array();

            return cipher.doFinal(byteArray);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    } // encriptar()


    public static String decifrarTexto(byte[] cipherTextArray, PrivateKey privateKey) {

        try {

            Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWITHSHA-512ANDMGF1PADDING");
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            byte[] decryptedTextArray = cipher.doFinal(cipherTextArray);

            return new String(decryptedTextArray);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    } // decifrar()


    public static Bitmap decifrarImagen(byte[] cipherImageArray, PrivateKey privateKey) {

        try {

            Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWITHSHA-512ANDMGF1PADDING");
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            byte[] decryptedImageArray = cipher.doFinal(cipherImageArray);

            return BitmapFactory.decodeByteArray(decryptedImageArray, 0, decryptedImageArray.length);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    } // decifrar()

} // EncriptadoRSA
