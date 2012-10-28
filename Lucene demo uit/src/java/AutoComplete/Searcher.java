/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package AutoComplete;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Set;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.queryParser.ParseException;

/**
 *
 * @author Dungit86
 */
public class Searcher {

    public static void main(String[] args)
            throws CorruptIndexException, IOException, ParseException {
        
        String queryText="time";

        SearchInstance SearchInstance = new SearchInstance();
        System.out.println(queryText);
        ArrayList<AutocompleteDTO> List = new ArrayList<AutocompleteDTO>();
        List = SearchInstance.Search(queryText);
        String jesonArry = getJsonArryForAutocmplt(List);
        //out.println(List);
        String result = "{\"suggestions\":[" + jesonArry + "]}";
        System.out.println(result);
    }

    public static String getJsonArryForAutocmplt(ArrayList<AutocompleteDTO> List) {
        String suggestions = "";
        int limit = 0;
        if (List.size() > 8) {
            limit = 8;
        } else {
            limit = List.size();
        }
        for (int i = 0; i < List.size(); i++) {
            if (List.get(i).getScore() > 0.3) {
                suggestions += List.get(i).getScore() + "@ " + List.get(i).getIdObject() + "@ " + List.get(i).getObjectName() + "@ " + List.get(i).getType() + "\r\n";
            }
        }
        if (suggestions == "") {
            suggestions += List.get(0).getScore() + "@ " + List.get(0).getIdObject() + "@ " + List.get(0).getObjectName() + "@ " + List.get(0).getType() + "\r\n";
        }
        if (suggestions != "") {
            suggestions = suggestions.substring(0, suggestions.length() - 1);
        }
        return suggestions;
    }

    public static ArrayList<AutocompleteDTO> Sort(ArrayList<AutocompleteDTO> List) {
        AutocompleteDTO temp = new AutocompleteDTO();
        for (int i = 0; i < List.size(); i++) {
            for (int j = i + 1; j < List.size(); j++) {
                if (List.get(i).getScore() < List.get(j).getScore()) {
                    temp = new AutocompleteDTO();
                    temp = List.get(i);
                    List.set(i, List.get(j));
                    List.set(j, temp);
                    continue;
                }
            }
        }
        return List;
    }
}
