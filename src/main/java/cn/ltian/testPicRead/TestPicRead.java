package cn.ltian.testPicRead;


import com.java4less.ocr.docparser.DocumentDef;
import com.java4less.ocr.docparser.Parser;
import com.java4less.ocr.docparser.data.Document;
import com.java4less.ocr.docparser.data.DocumentSet;
import com.java4less.ocr.docparser.data.Section;
import com.java4less.ocr.tess3.OCRFacade;
public class TestPicRead {


    /**
     * @param args
     */
    public static void main2(String[] args) {
        OCRFacade facade=new OCRFacade();
        try {
            String text=facade.recognizeFile("D:\\ltian\\workspaceGithub\\parsePic\\order.png", "eng");

            System.out.println("Text in the image is: ");
            System.out.println(text);

        } catch (Exception e) {
            e.printStackTrace();
        }


//        String str = "12222345";
//        if(str.length()>5){
//            str = str.substring(0,5);
//        }
//
//        System.out.println(str);
    }

    public static void main(String[] args) {

        try {
            OCRFacade facade=new OCRFacade();
            String text=facade.recognizeFile("C:\\Users\\ltian\\Pictures\\临时\\42c054b084504a11ab8b56538176736c.jpg", "eng");

            System.out.println("\n\npic out=\n"+text);

            DocumentDef docDef=new DocumentDef();
            docDef.loadFromXml("D:\\ltian\\workspaceGithub\\parsePic\\ordedef.xml");

            Parser parser=new Parser(docDef);
            DocumentSet docSet=parser.parse(text);

            if (docSet.getCount()<1) {
                System.err.println("No purchase order found");
                System.exit(-1);
            }

            Document doc=docSet.getDocument(0); // we assume each image has one order only

            // print order information to standard output
            System.out.println("Purchase order number: "+doc.getSectionByName("order header")[0].getField("number"));
            System.out.println("Delivery date: "+doc.getSectionByName("order header")[0].getField("DeliveryDate"));
            Section[] items=doc.getSectionByName("items detail");
            for (int i=0;i<items.length;i++) {
                System.out.println("article= "+items[i].getField("Article")+" quantity= "+items[i].getField("Quantity"));
            }

            // another option, create XML file for the extracted data
            System.out.println("\n\nXML output=\n"+docSet.toXml());



        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
