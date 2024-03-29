
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Parser {
    static int idx=0;
    static StringBuilder sb=new StringBuilder();
    public static void main(String[] args) {
        StringBuilder stringBuilder = new StringBuilder();
            Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
                stringBuilder.append(line).append("\n");
            
        }

        
        
        String fileContents = stringBuilder.toString(); 
        if(fileContents.isEmpty()){
            System.out.print("[]");
            System.exit(0);
        }
        sb.append("[");
        parseString(fileContents,idx);
        sb.append("]");

        String result = sb.toString().replaceAll(",(?=[}\\])]\\s*)", "");
        System.out.println(result);
    }

    private static void parseString(String fileContents,int i) {
        if(!isValid(fileContents)){
            System.out.println("Invalid Parantheses");
            throw new RuntimeException("Error:Invalid Parantheses");
        }
        if(fileContents.isEmpty()){
            return;
        }
        while(i<fileContents.length()){
            i=parseInput(fileContents,i);
            i++;
        }

    }

    private static int parseInput(String fileContents, int i) {

        if(fileContents.isEmpty()){
            return i;
        }
        if (fileContents.charAt(i) == '#') {
            while (i < fileContents.length() && fileContents.charAt(i) != '\n') {
                i++;
            }
            return i;
        }
        if(Character.isLetter(fileContents.charAt(i))|| Character.isDigit(fileContents.charAt(i))){
            i=processInteger(fileContents,i);
        }
        else if(fileContents.charAt(i)==':'){
            i=processAtom(fileContents,i);
        }else if(fileContents.charAt(i)=='['){
            i=processList(fileContents,i);
        }else if(fileContents.charAt(i)=='{'){
            i=processTuple(fileContents,i);
        }else if(fileContents.charAt(i)=='%'){
            i=processMap(fileContents,i);
        }
        return i;
    }

    private static int processMap(String fileContents, int i) {
        i++;
        int ptr=i;
        String s=fileContents.substring(i,ptr+1);
        while(ptr<fileContents.length() && !isValid(s)){
            ptr++;
            s=fileContents.substring(i,ptr);
        }


        sb.append("{\"%k\":\"map\",\"%v\":[");
        int si=1;
        si=removeSpacesAndComments(s,si);
        while(s.charAt(si)!='}'){
            sb.append("[");
            si=removeSpacesAndComments(s,si);
            si=parseInput(s,si);
            si=removeSpacesAndComments(s,si);
            boolean flag=false;
            int x=si;
            while(x>=0){
                if(s.charAt(x)==':'){
                    flag=true;
                    break;
                }else if(s.charAt(x)==' '){
                    x--;
                }else {break;}
            }
            if(s.charAt(si)!='='&&flag){throw new RuntimeException();}else if(s.charAt(si)=='=')si++;
            si=removeSpacesAndComments(s,si);
            if(s.charAt(si)=='>')si++;
            si=removeSpacesAndComments(s,si);
            si=removeSpacesAndComments(s,si);
            si=parseInput(s,si);
            si=removeSpacesAndComments(s,si);
            sb.append("],");
            si=removeSpacesAndComments(s,si);
            if(s.charAt(si)==','){
                si++;
                si=removeSpacesAndComments(s,si);
                if(s.charAt(si)=='}')throw new RuntimeException();
            }
        }sb.append("]},");
        i=ptr;
        return i;
    }

    private static int processTuple(String fileContents, int i) {
        int ptr=i;
        String s=fileContents.substring(i,ptr+1);
        while(ptr<fileContents.length() && !isValid(s)){
            ptr++;
            s=fileContents.substring(i,ptr+1);
        }
        sb.append("{\"%k\":\"tuple\",\"%v\":[");
        int si=1;
        while(s.charAt(si)!='}'){
            si=removeSpacesAndComments(s,si);
            si=parseInput(s,si);
            si=removeSpacesAndComments(s,si);
            if(s.charAt(si)==','){
                si++;
                if(s.charAt(si)=='}'){
                    throw new RuntimeException();
                }
            }
        }
        sb.append("]},");
        i=ptr+1;
        if(fileContents.charAt(i)=='{')return ptr;
        return i;
    }

    private static int processList(String fileContents, int i) {
        int ptr=i;
        String s=fileContents.substring(i,ptr+1);
        while(ptr<fileContents.length() && !isValid(s)){
            ptr++;
            s=fileContents.substring(i,ptr+1);
        }

//                if(s.isEmpty()){
//                    System.out.println("Error: List is Empty");
//                    System.exit(0);
//                }
        sb.append("{\"%k\":\"list\",\"%v\":[");
        int si=1;
        while(s.charAt(si)!=']'){
            si=removeSpacesAndComments(s,si);
            si=parseInput(s,si);
            si=removeSpacesAndComments(s,si);
            if(s.charAt(si)==','){
                si++;
                if(s.charAt(si)==']'){
                    throw new RuntimeException();
                }
            }
        }

        sb.append("]},");

        i=ptr+1;
        if(fileContents.charAt(i)=='[')return ptr;
        return i;
    }

    private static int processAtom(String fileContents, int i) {
        i++;
        int last=processAlphanumericString(fileContents,i);
        String s=fileContents.substring(i,last);
        s=":"+s.replaceAll("\\s", "");
        sb.append("{\"%k\":\"atom\",\"%v\":\""+s+"\"},");
        i=last;
        return i;
    }

    private static int processInteger(String fileContents, int i) {
        int last=processAlphanumericString(fileContents, i);
        String s=fileContents.substring(i,last);
        if(s.matches("\\d[_\\d]*")){
            if(s.charAt(s.length()-1)=='_'){
                throw new RuntimeException("Error :Invald Integer");
            }
            s = s.replace("_", "");
            sb.append("{\"%k\":\"int\",\"%v\":"+s+"},");
            i=last;
            return i;
        }
        else if (s.equals("true") || s.equals("false")){
            sb.append("{\"%k\":\"bool\",\"%v\":"+s+"},");
            i=last;
            return i;
        }i=last;
        if(i<fileContents.length() && fileContents.charAt(i)==':'){
            s=":"+s.replaceAll("\\s", "");
            sb.append("{\"%k\":\"atom\",\"%v\":\""+s+"\"},");
            i++;
        }else {
            throw new RuntimeException("Error : String value invalid to process");
        }
        return i;
    }

    private static int removeSpacesAndComments(String s, int i) {
        while (i < s.length() && (s.charAt(i)==' ') || (s.charAt(i) == '#') ||(s.charAt(i) == '\n')) {
            if (s.charAt(i) == '#') {
                while (i < s.length() && s.charAt(i) != '\n') {
                    i++;
                }
            } else {
                i++;
            }
        }while (i < s.length() && (s.charAt(i)==' ') || (s.charAt(i) == '#'|| s.charAt(i) == '\n')) {
            if (s.charAt(i) == '#') {
                while (i < s.length() && s.charAt(i) != '\n') {
                    i++;
                }
            } else {
                i++;
            }
        }
        return i;
    }



    private static int processAlphanumericString(String fileContents, int i) {
        int ptr=i;
        while(ptr<fileContents.length()){
            if(Character.isLetter(fileContents.charAt(ptr))|| Character.isDigit(fileContents.charAt(ptr))|| fileContents.charAt(ptr)=='_'|| fileContents.charAt(i)==' '){
                ptr++;
            }else{
                break;
            }
        }
        return ptr;
    }

    public static boolean isValid(String s) {
        Stack<Character> stack = new Stack<>();
        Map<Character, Character> mapping = new HashMap<>();
        mapping.put(')', '(');
        mapping.put('}', '{');
        mapping.put(']', '[');

        for (char c : s.toCharArray()) {
            if (mapping.containsValue(c)) {
                stack.push(c);
            } else if (mapping.containsKey(c)) {
                if (stack.isEmpty() || stack.pop() != mapping.get(c)) {
                    return false;
                }
            }
        }

        return stack.isEmpty();
    }
}
