/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package DemoParseString;

import AutoComplete.AutocompleteDTO;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author dungit
 */
public class ParseString {
    /*
     static ArrayList<AutocompleteDTO> ObjectList = new ArrayList<AutocompleteDTO>();
     static ArrayList<String> freeWord = new ArrayList<String>();
     static AutocompleteDTO ObjectTemp = new AutocompleteDTO();
     //static String delims = "[ .,?!()-]+";
     static String Parse = "[ =(]+";
     static String Parse2 = "[)]+";
     static String[] st = null;
     */

    public static void main(String[] args) throws IOException {
        String ObjectName = "";
        ArrayList<AutocompleteDTO> ObjectList = new ArrayList<AutocompleteDTO>();
        ArrayList<String> freeWord = new ArrayList<String>();
        AutocompleteDTO ObjectTemp = new AutocompleteDTO();
        String Parse = "[ =(]+";
        String Parse2 = "[)]+";
        String Parse3 = "[ ]+";
        String[] st = null;
        String word = "  multi (timel) big key=(time delay) org=() org=(Microsoft) jour=(Journal of The ACM) conf=(SIGGRAPH) author=(Bill Gates) long (time) many year=2000 time year>=2006 song year<=2011 multi";
        //String word = " time delay";
        st = word.split(Parse);

        for (String Temp : st) {
            if (Temp.length() == 0) {
                continue;
            }
            if ("org".equals(Temp)) {
                if (!"".equals(ObjectName)) {
                    ObjectName = ObjectName.substring(0, ObjectName.length() - 1);
                    //System.out.println(ObjectName);
                    ObjectTemp.setObjectName(ObjectName);
                    ObjectList.add(ObjectTemp);
                    ObjectTemp = new AutocompleteDTO();
                    ObjectName = "";
                }
                //System.out.print("oganization=");
                ObjectTemp.setType("oganization");
                continue;
            }
            if ("jour".equals(Temp)) {
                if (!"".equals(ObjectName)) {
                    ObjectName = ObjectName.substring(0, ObjectName.length() - 1);
                    //System.out.println(ObjectName);
                    ObjectTemp.setObjectName(ObjectName);
                    ObjectList.add(ObjectTemp);
                    ObjectTemp = new AutocompleteDTO();
                    ObjectName = "";
                }
                //System.out.print("jour=");
                ObjectTemp.setType("journal");
                continue;
            }
            if ("conf".equals(Temp)) {
                if (!"".equals(ObjectName)) {
                    ObjectName = ObjectName.substring(0, ObjectName.length() - 1);
                    //System.out.println(ObjectName);
                    ObjectTemp.setObjectName(ObjectName);
                    ObjectList.add(ObjectTemp);
                    ObjectTemp = new AutocompleteDTO();
                    ObjectName = "";
                }
                //System.out.print("conf=");
                ObjectTemp.setType("conference");
                continue;
            }
            if ("author".equals(Temp)) {
                if (!"".equals(ObjectName)) {
                    ObjectName = ObjectName.substring(0, ObjectName.length() - 1);
                    //System.out.println(ObjectName);
                    ObjectTemp.setObjectName(ObjectName);
                    ObjectList.add(ObjectTemp);
                    ObjectTemp = new AutocompleteDTO();
                    ObjectName = "";
                }
                //System.out.print("author=");
                ObjectTemp.setType("author");
                continue;
            }
            if ("key".equals(Temp)) {
                if (!"".equals(ObjectName)) {
                    ObjectName = ObjectName.substring(0, ObjectName.length() - 1);
                    //System.out.println(ObjectName);
                    ObjectTemp.setObjectName(ObjectName);
                    ObjectList.add(ObjectTemp);
                    ObjectTemp = new AutocompleteDTO();
                    ObjectName = "";
                }
                //System.out.print("key=");
                ObjectTemp.setType("keyword");
                continue;
            }
            if ("year".equals(Temp)) {
                if (!"".equals(ObjectName)) {
                    ObjectName = ObjectName.substring(0, ObjectName.length() - 1);
                    //System.out.println(ObjectName);
                    ObjectTemp.setObjectName(ObjectName);
                    ObjectList.add(ObjectTemp);
                    ObjectTemp = new AutocompleteDTO();
                    ObjectName = "";
                }
                //System.out.print("key=");
                ObjectTemp.setType("year");
                continue;
            }
            if ("year>".equals(Temp)) {
                if (!"".equals(ObjectName)) {
                    ObjectName = ObjectName.substring(0, ObjectName.length() - 1);
                    //System.out.println(ObjectName);
                    ObjectTemp.setObjectName(ObjectName);
                    ObjectList.add(ObjectTemp);
                    ObjectTemp = new AutocompleteDTO();
                    ObjectName = "";
                }
                //System.out.print("key=");
                ObjectTemp.setType("year>");
                continue;
            }
            if ("year<".equals(Temp)) {
                if (!"".equals(ObjectName)) {
                    ObjectName = ObjectName.substring(0, ObjectName.length() - 1);
                    //System.out.println(ObjectName);
                    ObjectTemp.setObjectName(ObjectName);
                    ObjectList.add(ObjectTemp);
                    ObjectTemp = new AutocompleteDTO();
                    ObjectName = "";
                }
                //System.out.print("key=");
                ObjectTemp.setType("year<");
                continue;
            }
            ObjectName += Temp + " ";
        }
        if (!"".equals(ObjectName)) {
            ObjectName = ObjectName.substring(0, ObjectName.length() - 1);
            //System.out.println(ObjectName);
            ObjectTemp.setObjectName(ObjectName);
            ObjectList.add(ObjectTemp);
            ObjectTemp = new AutocompleteDTO();
            ObjectName = "";
        }
        System.out.println("------------------------------------------");
        for (int k = 0; k < ObjectList.size(); k++) {
            //System.out.println(ObjectList.get(k).getType() + ":" + ObjectList.get(k).getObjectName());
            System.out.println(ObjectList.get(k).getType() + ":" + ObjectList.get(k).getObjectName());
        }

//-------------------------------------------------------------------------------------------------------
        System.out.println("------------------------------------------");
        for (int k = 0; k < ObjectList.size(); k++) {
            if ("year".equals(ObjectList.get(k).getType()) || "year>".equals(ObjectList.get(k).getType()) || "year<".equals(ObjectList.get(k).getType())) {
                String[] parseYear = ObjectList.get(k).getObjectName().split(Parse3);
                if (parseYear.length >= 1) {
                    ObjectList.get(k).setObjectName(parseYear[0]);
                    if (parseYear.length > 1) {
                        for (int j = 1; j < parseYear.length; j++) {
                            freeWord.add(parseYear[j].substring(1, parseYear[j].length()));
                        }
                    }
                } else {
                    ObjectList.remove(k);
                    k--;
                }
            } else {
                String[] parse = ObjectList.get(k).getObjectName().split(Parse2);
                if (!"".equals(ObjectList.get(k).getType())) {

                    if (parse.length >= 1) {
                        ObjectList.get(k).setObjectName(parse[0]);
                        if (parse.length > 1) {
                            for (int j = 1; j < parse.length; j++) {
                                freeWord.add(parse[j].substring(1, parse[j].length()));
                            }
                        }
                    } else {
                        ObjectList.remove(k);
                        k--;
                    }
                    for (String temp1 : parse) {
                        System.out.println("*" + temp1);
                    }
                } else {
                    freeWord.add(ObjectList.get(k).getObjectName().replace(")", ""));
                    ObjectList.remove(k);
                    k--;
                }
            }
        }

//-------------------------------------------------------------------------------------------------------
        System.out.println("------------------------------------------");
        for (int k = 0; k < ObjectList.size(); k++) {
            System.out.println(ObjectList.get(k).getType() + ":" + ObjectList.get(k).getObjectName());
        }
        System.out.println("FREE WORD:");
        for (String temp3 : freeWord) {
            System.out.println("-" + temp3);
        }
    }
}
