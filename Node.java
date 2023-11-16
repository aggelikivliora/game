package ce326.hw3;
import static java.lang.Integer.MAX_VALUE;
import static java.lang.Integer.MIN_VALUE;
public class Node{
    private int value;
    Node parent;
    char type;
    Node []array;
    int[] position;
    int alpha;
    int beta;
    
    public Node() 
    {
        value=0; 
        parent=null;
        array = null;
        position = new int[2];
        alpha = MIN_VALUE;
        beta = MAX_VALUE;
        //type=null;
    }
    public Node(Node []array)
    {
        this.array = array;
        alpha = MIN_VALUE;
        beta = MAX_VALUE;
    }
    public void setPosition(int[] pos)      
    {
        this.position[0] = pos[0];
        this.position[1] = pos[1];
    }
    public int[] getPosition()      
    {
        return position;
    }
    public void setType(char type)      
    {
        this.type = type;
    }
    public char getType()      
    {
        return type;
    }
    
    public int getValue(){
        return value;
    }
    public void setValue(int value){
        this.value = value;
    }
    
    public void setParent(Node nd){
        parent=nd;
    }
    public Node getParent(){
        return parent;
    }
    
    public void setChildrenSize(int size)
    {
        this.array = new Node[size];
    }
    public int getChildrenSize()
    {
        return array.length;
    }
    
    public void insertChild(int pos, Node X)
    {
        array[pos] = X;
    }
    
    public Node getChild(int pos)
    {
        return array[pos];
    }
    public Node maximizer(){
        Node curr;
        curr = array[0];
        if(array.length>1){
            for(int i=1; i<array.length; i++){
                if(array[i].getValue()>curr.getValue()){    
                    curr.setValue(array[i].getValue());
                    curr.setPosition(array[i].getPosition());
                }
            }
        }
        else{
            return curr;
        }
        return curr;
    }
    public Node minimizer(){
        Node curr;
        curr = array[0];
        if(array.length>1){
            for(int i=1; i<array.length; i++){ 
                if(array[i].getValue()<curr.getValue()){    
                    curr.setValue(array[i].getValue());
                    curr.setPosition(array[i].getPosition());
                }
            }
        }
        else{
            return curr;
        }
        return curr;
    }
}
