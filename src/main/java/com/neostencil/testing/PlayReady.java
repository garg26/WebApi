package com.neostencil.testing;

import java.util.Date;
import javax.management.timer.Timer;
import keyos.authxml.generator.AuthXMLGenerator;
import keyos.authxml.generator.License;
import keyos.authxml.generator.utils.XElement;

/**
 * The code below creates authentication XML to acquire a non-persistent license which is valid for
 * one play and is not stored on the client side.
 *
 * The purpose of the code to show you minimum required code to acquire a license during your tests.
 * To know more about possible properties and values, please check Wiki in your KeyOS Account.
 */

public class PlayReady {

  public static void main(String[] args) {
    try {
      // Create KeyOSAuthXMLGenerator class instance
      AuthXMLGenerator authXmlGenerator = new AuthXMLGenerator();

      authXmlGenerator.setPrivateKey("src/main/resources/test_ns");
      authXmlGenerator.setRsaPubKeyId("test_ns");

      // Set authentication XML expiration time in UTC. For production you would want to set this
      // value to time enough to acquire
      // a license (1 minute or less). For test purposes especially if you don't regenerate the
      // authentication XML and use
      // it as static value on your HTML page (which you should not do in production), you can set
      // the time to a day or week etc...
      authXmlGenerator
          .setAuthXMLExpirationTime(new Date(new Date().getTime() + 2L * Timer.ONE_MINUTE));

      // Create PlayReady License (simple license, not a root or leaf license).
      License simpleLicense = new License();

      // Add license description into generator.
      authXmlGenerator.getLicenses().add(simpleLicense);

      // Ready to be used - Base64-encoded XML
      System.out.println("Base64-encoded Signed Authentication XML:");
      XElement generatedXML = authXmlGenerator.generateAuthenticationXML();
      XElement signedAuthXML = authXmlGenerator.SignAuthenticationXML(generatedXML);
      System.out.println(authXmlGenerator.EncodeAuthenticationXML(signedAuthXML));

      // Debug output - formatted XML
      System.out.println("\n\nDebug output - formatted XML:");
      // System.out.println(authXmlGenerator.prettyXML(authXmlGenerator.SignAuthenticationXML(authXmlGenerator.generateAuthenticationXML())));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
