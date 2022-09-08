package sample;

public class RLE {

    public static String Compress(String word)
    {
        String res="";
        int i=0;
        while (i<word.length())
        {
            int cnt=0;
            char c=word.charAt(i);
            while (i<word.length() && c==word.charAt(i)) {
                i++;
                cnt++;
            }
            if (cnt>1){
                res+="*";
                res+=cnt;
            }
            res+=c;
        }
        return res;
    }

    public static String DeCompress(String compressed)
    {
        String res="";
        for (int i=0;i<compressed.length();i++)
        {
            if (compressed.charAt(i)=='*')
            {
                i++;
                int num=0;
                while (Character.isDigit(compressed.charAt(i))) {
                    num*=10;
                    num+=compressed.charAt(i)-'0';
                    i++;
                }
                while (num!=0)
                {
                    res+=compressed.charAt(i);
                    num--;
                }
            }
            else res+=compressed.charAt(i);
        }
        return res;
    }
}
