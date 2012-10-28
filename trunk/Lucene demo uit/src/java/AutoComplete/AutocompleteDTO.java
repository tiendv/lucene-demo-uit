/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package AutoComplete;

/**
 *
 * @author dungit
 */
public class AutocompleteDTO {

    private String idObject;
    private String objectName;
    private float Score;
    private String Type;

    public AutocompleteDTO() {
    idObject="";
    objectName="";
    Type="";
}
public void setIdObject(String idObject) 
    {
        this.idObject = idObject;
    }
    public String getIdObject()
    {
        return this.idObject;
    }
    
    public void setObjectName(String ObjectName) 
    {
        this.objectName = ObjectName;
    }
    public String getObjectName()
    {
        return this.objectName;
    }
    
    public void setScore(float Score) 
    {
        this.Score = Score;
    }
    public float getScore()
    {
        return this.Score;
    }
    
    public void setType(String Type) 
    {
        this.Type = Type;
    }
    public String getType()
    {
        return this.Type;
    }
}
