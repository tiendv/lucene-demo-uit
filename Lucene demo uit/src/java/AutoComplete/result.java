/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package AutoComplete;

import DemoSpellchecker.LuceneSpellSearcher;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.queryParser.ParseException;

/**
 *
 * @author dungit
 */
public class result extends HttpServlet {

    /**
     * Processes requests for both HTTP
     * <code>GET</code> and
     * <code>POST</code> methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, CorruptIndexException, ParseException, Exception {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        String queryText = null;
        try {
            if (!"".equals(request.getParameter("text"))) {
                queryText = URLDecoder.decode(request.getParameter("text"), "utf-8");
                //queryText = request.getParameter("text");
            }
            /*
             LuceneSearcher luceneSearcher = null;
             String lucenePath = "C://INDEX-KEYWORD";
             String lucenePath2 = "C://INDEX-AUTHOR";
             if (luceneSearcher == null) {
             luceneSearcher = new LuceneSearcher(lucenePath, lucenePath2);
             }
             ArrayList<AutocompleteDTO> List = luceneSearcher.search(queryText);
             List = Sort(List);
             */
            //SearchInstance SearchInstance = new SearchInstance();
            ArrayList<AutocompleteDTO> List = new ArrayList<AutocompleteDTO>();
            //List.addAll(SearchInstance.Search(queryText));
            
            LuceneSpellSearcher Searcher = new LuceneSpellSearcher();
            List.addAll(Searcher.suggest(queryText));
            List = Sort(List);
            String jesonArry = getJsonArryForAutocmplt(List);
            String result = "{\"suggestions\":[" + jesonArry + "]}";
            out.println(result);

        } finally {
            out.close();
        }
    }

    public String getJsonArryForAutocmplt(ArrayList<AutocompleteDTO> List) {
        String suggestions = "";
        if (List.size() == 0) {
            suggestions = " ";
        }
        for (int i = 0; i < List.size(); i++) {
            System.out.println(List.get(i).getObjectName());
            suggestions += "{\"name\":\""  + List.get(i).getObjectName() + "\",\"icon\":\"" + List.get(i).getType() + ".jpg\" },";
            //suggestions += "{\"name\":\"" + List.get(i).getScore() + " " + List.get(i).getObjectName() + "\",\"icon\":\"" + List.get(i).getType() + ".jpg\" },";
            //suggestions += "{\"icon\":\"" + ListString[0] + "\"},";
        }
        suggestions = suggestions.substring(0, suggestions.length() - 1);
        return suggestions;
    }

    public ArrayList<AutocompleteDTO> Sort(ArrayList<AutocompleteDTO> List) {
        AutocompleteDTO temp = new AutocompleteDTO();
        for (int i = 0; i < List.size(); i++) {
            for (int j = i + 1; j < List.size(); j++) {
                if (List.get(i).getScore() > List.get(j).getScore()) {
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

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP
     * <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, CorruptIndexException {
        try {        
            processRequest(request, response);
        } catch (ParseException ex) {
            Logger.getLogger(result.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(result.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    /**
     * Handles the HTTP
     * <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, CorruptIndexException {
        try {
            processRequest(request, response);
        } catch (ParseException ex) {
            Logger.getLogger(result.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(result.class.getName()).log(Level.SEVERE, null, ex);
        }
          
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
