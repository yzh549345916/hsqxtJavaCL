package yzh.天擎.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

public class FormatUtil {

  public void outputRstXml( String xml ) {
    String formatXml = null;  
    
    SAXReader reader = new SAXReader();  
    try {
      Document document = reader.read(new StringReader(xml));
      XMLWriter writer = null;  
      
      if (document != null) {
        StringWriter stringWriter = new StringWriter();  
        OutputFormat format = new OutputFormat(" ", true);  
        writer = new XMLWriter(stringWriter, format);  
        writer.write(document);  
        writer.flush();  
        formatXml = stringWriter.getBuffer().toString();
      }      
      
    } catch( IOException e ) {
      e.printStackTrace();      
    } catch (DocumentException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } 

    this.output( formatXml );    
  }
  
  public void outputRstJson( String json ) {
    String formatJson = null ;
    
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    JsonParser jp = new JsonParser();
    JsonElement je = jp.parse(json);
    formatJson = gson.toJson(je);

    this.output( formatJson );
  }  

  public void outputRstHtml( String html ) {
    this.outputRstXml( html );
  }

  public void outputRstText( String text ) {
    this.output( text );
  }
  
  private void output( String str ) {
    if( str != null ) {
      //DEMO中，只输出前1000个字母
      if( str.length() < 2000 ) {
        System.out.println( str ) ;
      } else {
        System.out.println( str.substring( 0, 2000 ) ) ;
        System.out.println( ".........." ) ;
      }      
    }    
  }
  
}
