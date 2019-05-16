package cn.ltian.testPicRead;

public class TestPicRead {


    /**
     * @param args
     */
    public static void main(String[] args) {
//        OCRFacade facade=new OCRFacade();
//        try {
//            String text=facade.recognizeFile("C:\\Users\\ltian\\Pictures\\临时\\1.jpg", "eng");
//
//            System.out.println("Text in the image is: ");
//            System.out.println(text);
//
//        } catch (Exception e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }


        String str = "12222345";
        if(str.length()>5){
            str = str.substring(0,5);
        }

        System.out.println(str);
    }
}
