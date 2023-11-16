package ce326.hw3;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class GamePlay {
    char [][]status ;//= new int[6][7];
    int mode ;//= new String();
    ArrayList<int[]> steps;
    //mia lista me th seira pou eginan oi kiniseis 
    // argotera thn pernaw se file me morfh json
       
    public GamePlay(int mode){
        status = new char[6][7]; // P: PLAYER    A:AI
        for(int i=0; i<6; i++){
            for(int j=0; j<7; j++){
                status[i][j]='E';     // E: EMPTY
            }
        }
        this.mode = mode;
        steps = new ArrayList<>();
    }
    
    public void addPlayersPos(int x, int y){
        status[x][y] = 'P';
        int[] nxt = new int[2];
        nxt[0]=x;
        nxt[1]=y;
        steps.add(nxt);
    }
    public void addAIPos(int x, int y){
        status[x][y] = 'A';
        int[] nxt = new int[2];
        nxt[0]=x;
        nxt[1]=y;
        steps.add(nxt);
    }
    public void createFile(String filepath, String filename) throws IOException{
        filename=filename.concat(".json");
        File thisGame = new File(filepath, filename);
        thisGame.createNewFile();
        try{
            FileWriter myWriter = new FileWriter(thisGame);
            StringBuilder str = new StringBuilder();
            str.append("{\n");
            str.append("  ");
            str.append('"');
            str.append("steps");
            str.append('"');
            str.append(": [\n");
            for(int i=0; i<steps.size(); i++){
                str.append("    {\n");
                str.append("      ");
                str.append('"');
                str.append("child");
                str.append('"');
                str.append(": [\n");
            
                str.append("        {\n");
                str.append("            ");
                str.append('"');
                str.append("x");
                str.append('"');
                str.append(": ");
                str.append(steps.get(i)[0]);
                str.append(",\n");
                
                str.append("            ");
                str.append('"');
                str.append("y");
                str.append('"');
                str.append(": ");
                str.append(steps.get(i)[1]);
                str.append("\n        }\n      ]\n    }");
                if(i< (steps.size()-1)){str.append(",");}
                str.append("\n"); 
            }
            str.append("  ]\n}");
            myWriter.write(str.toString());
            myWriter.close();
        }
        catch(IOException ex){
            
        }
    }
    public int[] nextMove(int[] next){
        minMaxOptimalTree move = new minMaxOptimalTree(status,mode);
        next = move.minMax();
        return next;
    }
    public void reset(){
        for(int i=0; i<6;i++){
            for(int j=0; j<7; j++){
                if(status[i][j]!='E'){
                    status[i][j]='E';
                }
            }
        }
    }
    public int win(){
        minMaxOptimalTree move = new minMaxOptimalTree();
        if(move.cost('P', 'A', status)>1000){
            return 1;
        }
        if(move.cost('A', 'P', status)>1000){
            return 0;
        }
        return 3;
    }
}
