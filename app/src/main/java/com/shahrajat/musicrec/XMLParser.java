package com.shahrajat.musicrec;

import android.util.Log;
import android.widget.TextView;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import static android.R.attr.path;

/**
 * Created by rajatshah on 11/1/16.
 */

public class XMLParser {
    public Document getDomElement(String xml){
        Document doc = null;
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {

            DocumentBuilder db = dbf.newDocumentBuilder();

            InputSource is = new InputSource();
            is.setCharacterStream(new StringReader(xml));
            doc = db.parse(is);

        } catch (ParserConfigurationException e) {
            Log.e("Error: ", e.getMessage());
            return null;
        } catch (SAXException e) {
            Log.e("Error: ", e.getMessage());
            return null;
        } catch (IOException e) {
            Log.e("Error: ", e.getMessage());
            return null;
        }
        // return DOM
        return doc;
    }

    public String getXmlFromUrl(TextView errBox){
        System.out.print("***CALLING XML");
        StringBuilder sb = new StringBuilder();
        ///Users/rajatshah/AndroidStudioProjects/MusicRec/app/src/main/java/com/shahrajat/musicrec/XMLParser.java
        ///Users/rajatshah/AndroidStudioProjects/MusicRec/app/src/main/res/xml/songs.xml

        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        URL url = classLoader.getResource("res/xml/songs.xml");
        errBox.setText(url.toString());

        try (BufferedReader br = new BufferedReader(new FileReader("///Users/rajatshah/AndroidStudioProjects/MusicRec/app/src/main/res/xml/songs.xml"))){
            String sCurrentLine = br.readLine();
            while (sCurrentLine != null) {
                sb.append(sCurrentLine);
                sCurrentLine = br.readLine();
            }
        } catch (IOException io) {

            errBox.setText(io.getMessage());
        }

        return sb.toString();   //return XML
    }

    public String getValue(Element item, String str) {
        NodeList n = item.getElementsByTagName(str);
        return this.getElementValue(n.item(0));
    }

    public final String getElementValue( Node elem ) {
        Node child;
        if( elem != null){
            if (elem.hasChildNodes()){
                for( child = elem.getFirstChild(); child != null; child = child.getNextSibling() ){
                    if( child.getNodeType() == Node.TEXT_NODE  ){
                        return child.getNodeValue();
                    }
                }
            }
        }
        return "";
    }
}
