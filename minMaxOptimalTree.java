package ce326.hw3;
import static java.lang.Integer.MAX_VALUE;
import static java.lang.Integer.MIN_VALUE;
public class minMaxOptimalTree {
    //kataskeyasths : ftiaxnw dentro
     Node root;
    public minMaxOptimalTree(char[][] array, int mode){
        root=new Node();
        root.setType('X');
        root = buildTree(mode, root,array);
    }
    public minMaxOptimalTree(){
        root = null;
    }
    public void printree(Node nd, int vathos, int zhtoumeno){
        if(nd.getType()=='L'){return;}
        if(vathos==zhtoumeno){
            System.out.print("[");
            for(int i=0; i<nd.getChildrenSize(); i++){
                System.out.print(i+"("+nd.getChild(i).getValue()+") ");
            }
            System.out.print("]\n");
        }
        else{
            for(int i=0; i<nd.getChildrenSize(); i++){
                printree(nd.getChild(i),vathos+1, zhtoumeno);
            }
        }
    }
    //epilyetai o algori8mos minmax me alpha beta pruning
    public int[] minMax(){
        minMaxOptimalRecursive(root);
        return root.getPosition();
    }
    public Node buildTree(int mode, Node nd, char[][] status){ // mode: poses anadromes 8a ginoun (TRIVIAL=1, MEDIUM=3, HARD=5)
        Node nodeArray = new Node(); // apo8hkeuontai ta ofeloi
        nodeArray.setChildrenSize(7);
        int children=0;
        char opponent;
        char us;
        if((mode%2)==0){
            opponent = 'A'; // AI
            us = 'P';       // player
        }
        else{
            opponent = 'P';
            us = 'A';
        }
        int c = cost(opponent,us,status);
        int xy[] = new int[2];
        for(int i=0; i<7; i++){     // sthlh pou peftei to pouli
            int j;
            for(j=5; j>-1; j--){ // se ti va8os peftei
                if(status[j][i]=='E'){
                    xy[0]=j;
                    xy[1]=i;
                    status[j][i]=us;
                    break;
                }
            }
            if(j==-1){
                continue; // sthlh gemath
            }
            int benefit;
            int v= vertical(xy[0], xy[1],us,opponent,status);
            int h= horizontal(xy[0], xy[1],us,opponent,status,1);
            int l =lDiagonal(xy[0], xy[1],us,opponent,status,1);
            int u= uDiagonal(xy[0], xy[1],us,opponent,status,1);
            if(us=='P'){
                benefit = -((h+v+l+u)-c);
            }
            else{
                benefit = (h+v+l+u)-c;
            }
            if((mode==1)||((Math.abs(benefit)+c)>1000)){ // ama exw benefit+c >1000 exw 4 sth seira
                Node leaf = new Node();
                leaf.setParent(nd);
                leaf.setType('L');
                leaf.setValue(benefit);
                leaf.setPosition(xy);
                status[xy[0]][xy[1]]='E';
                nodeArray.insertChild(children, leaf);
                children++;
                continue;             
            }          
            Node inner = new Node();
            inner.setParent(nd);
            inner.setPosition(xy);
            if((mode%2)==0){
                inner.setType('X'); //maximizer
            }
            else{
                inner.setType('N'); //minimizer
            }
            nodeArray.array[i]=buildTree(mode-1, inner,status);
            nodeArray.insertChild(children, inner);
            children++;
            status[xy[0]][xy[1]]='E';
        }
        if(children>0){  // an children=0 tote o pinakas/kamvas einai foul
            nd.setChildrenSize(children);
            for(int i=0; i<children; i++){
                nd.insertChild(i, nodeArray.array[i]);
            }
        }
        else{
            nd.setType('L'); // afou den exei alla paidia(alles kinhseis)
        }
        return nd;
    }
    
    
    //####################### CALCULATE AIs COST ###############################
    public int cost(char requested, char other, char[][] status){
        int c=0;
        for(int i=0; i<6; i++){
            c = c + horizontal(i, 0,requested,other,status,0);
        }
        for(int i=0; i<7; i++){
            c = c + vertical(0, i,requested,other,status); 
        }
        for(int i=0; i<3; i++){
            if(i==0){
                for(int j=0; j<4; j++){
                    c = c + lDiagonal(0, j,requested,other,status,0);
                }
            }
            else{
                c = c + lDiagonal(i, 0,requested,other,status,0);
            }
        }
        for(int i=0; i<3; i++){
            if(i==0){
                for(int j=3; j<7; j++){
                    c = c + uDiagonal(0, j,requested,other,status,0);
                }
            }
            else{
                c = c + uDiagonal(i, 6,requested,other,status,0);
            }
        }
        //System.out.println(c);
        return c;
    }

    public int vertical(int x, int y, char requested, char other, char[][] status){
        int c=0;
        for(int i=0; i<3; i++){  // elegxw tis 3 ka8etes pou yparxoun sthn sthlh ths 8eshs(pouliou)
            if(x>(i+3)){continue;}  // an to pouli den anoikei se 4ada
            int plus=0;
            char curr=other;/// se periptwsh pou den uparxei kanena pouli
            for(int j=i; j<(i+4); j++){ //elegxw to ka8e pouli ths 4das
                curr = status[j][y];
                if((curr==other)){ //an uparxei kitrino (tou AI) break;
                    //System.out.print("ERROR(1)1 >>"+j+","+y+",    ");
                    break;
                }
                if(curr==requested){
                    switch(plus){
                        case 0 : 
                            plus=1;
                            break;
                        case 1 : 
                            plus=4;
                            break;
                        case 4 : 
                            plus=16;
                            break;
                        case 16 : 
                            plus=1000;
                            break;
                    }
                    //System.out.print("ERROR(1)2 >>"+j+","+y+",    <>"+plus);
                }
            }
            if(curr!=other){c = c+plus;}
        }
        //System.out.println("TEST Vertical: "+c);
        return c;
    }
    public int horizontal(int x, int y, char requested, char other, char[][] status, int benefit){  // benefit:flag:   0->kostos   1->ofelos(vash shmeiou)
        int c=0;
        for(int i=0; i<4; i++){  // elegxw tis 4 orizonties pou yparxoun sthn sthlh ths 8eshs(pouliou)
            if((y>(i+3))&&(benefit==1)){continue;}  // an to pouli den anoikei sauth 4ada
            if((y<i)&&(benefit==1)){break;}
            int plus=0;
            char curr=other;/// se periptwsh pou den uparxei kanena pouli
            for(int j=i; j<(i+4); j++){ //elegxw to ka8e pouli ths 4das
                curr = status[x][j];
                if((curr==other)){ //an uparxei kitrino (tou AI) break;
                    break;
                }
                if(curr==requested){
                    switch(plus){
                        case 0 : 
                            plus=1;
                            break;
                        case 1 : 
                            plus=4;
                            break;
                        case 4 : 
                            plus=16;
                            break;
                        case 16 : 
                            plus=1000;
                            break;
                    }
                }
            }
            if(curr!=other){c = c+plus;}
        }
        return c;
    }
    public int lDiagonal(int x, int y, char requested, char other, char[][] status, int benefit){   /// BELTISTOPOIHSH  !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        int c=0;
        int diagonal=Math.abs(x-y);
        if((!((diagonal>=3)&&(x>2)&&(y<3)))  &&  (!(((diagonal)>=4)&&(x<3)&&(y>3)))){  // an anoikei se diagonies me 8eseis>=4
            for(int i=0; i<=3; i++){  // elegxw tis 3 lower diagwnies pou yparxoun sth diagonio tou pouliou   an uper: i ---> x  lower: i--->y
                int plus=0;
                char curr=other;/// se periptwsh pou den uparxei kanena pouli
                if((x>=y)&& ((i+diagonal+3)>5)){break;} //lower i---> y
                if((x<y)&& ((i+diagonal+3)>6)){break;} //upper i---> x
                int flag=0;
                for(int j=i; j<(i+4); j++){
                    if(x>=y){
                        curr = status[j+diagonal][j];
                    }
                    else{
                        curr = status[j][j+diagonal];
                    }
                    if((curr==other)){ //an uparxei kitrino (tou AI) break;
                        break;
                    }
                    if(curr==requested){
                        if(benefit==1){
                            if((x>=y)&&((j+diagonal)==x)&&(j==y)){flag=1;}
                            if((x<y)&&((j)==x)&&((j+diagonal)==y)){flag=1;}
                        }else{
                            flag=1;
                        }
                        switch(plus){
                            case 0 : 
                                plus=1;
                                break;
                            case 1 : 
                                plus=4;
                                break;
                            case 4 : 
                                plus=16;
                                break;
                            case 16 : 
                                plus=1000;
                                break;
                        }
                    }
                }
                if((curr!=other)&&(flag==1)){c = c+plus;}
            }
        }
        return c;
    }
    
    public int uDiagonal(int x, int y, char requested, char other, char status[][], int benefit){
        int c=0;
        int []xy = new int[2]; 
        xy[0] = x;
        xy[1] = y;

        while((xy[0]!=0)&&(xy[1]!=6)){////////////////////// upper triangle 
            xy[0]--;
            xy[1]++;
        }
            
        int[] temp =new int[2];  // krata to 1o kouti ths curr diagoniou
        temp[0] = xy[0];
        temp[1] = xy[1];
        int times;
        if((xy[0]==0)&&(xy[1]<3)){return 0;}
        if((xy[1]==6)&&(xy[0]>2)){return 0;}
        if((xy[0]==0)&&(xy[1]!=6)){
            times= xy[1]-xy[0]-3;
        }
        else{
            times= xy[1]-xy[0]-4;
        }
        for(int i=0; i<=times; i++){ //3 diagonies max ,  1 min
            int plus=0;
            char curr=other;
            xy[0] = temp[0];
            xy[1] = temp[1];
            int flag=0;  // an x,y anoikei sthn 4ada
            for(int j=0; j<4; j++){ // 4ades
                curr = status[xy[0]][xy[1]];// mia 8esh katw
                xy[0]++;
                xy[1]--;
                if((curr==other)){ //an uparxei kitrino (tou AI) break;
                    break;
                }
                if(curr==requested){
                    if(benefit==1){
                        if(((xy[0]-1)==x)&&((xy[1]+1)==y)){flag=1;}
                    }
                    else{
                        flag=1;
                    }
                    switch(plus){
                        case 0 : 
                            plus=1;
                            break;
                        case 1 : 
                            plus=4;
                            break;
                        case 4 : 
                            plus=16;
                            break;
                        case 16 : 
                            plus=1000;
                            break;
                    }
                }
            }
            temp[0]++;
            temp[1]--;
            if((curr!=other)&&(flag==1)){c = c+plus;}
        }
        return c;
    }

    public void minMaxOptimalRecursive(Node nd){  //beta=MAX   alpha=MIN
        if(nd.getType()=='X'){
            for(int i=0; i<nd.getChildrenSize(); i++){
                nd.getChild(i).alpha=nd.alpha;
                nd.getChild(i).beta=nd.beta;
                minMaxOptimalRecursive(nd.getChild(i));
                if(nd.getChild(i).getValue()>nd.alpha)
                {
                    nd.alpha=nd.getChild(i).getValue();
                    if(nd.beta<=nd.alpha){
                        nd.setValue(nd.alpha);
                        return;
                    }
                }
            }
            Node value=nd.maximizer();    // epistrefei ton komvo me th megalyterh timh
            nd.setValue(value.getValue());
            if(nd.getParent()==null){    // mono otan exw riza pairnw to position (h riza panta maximizer)
                nd.setPosition(value.getPosition());
            }
            return;
        }
        if(nd.getType()=='N'){
            for(int i=0; i<nd.getChildrenSize(); i++){
                nd.getChild(i).beta=nd.beta;
                nd.getChild(i).alpha=nd.alpha;
                minMaxOptimalRecursive(nd.getChild(i));
                if(nd.getChild(i).getValue()<nd.beta)
                {
                    nd.beta=nd.getChild(i).getValue();
                    if(nd.beta<=nd.alpha){
                        nd.setValue(nd.beta);
                        return;
                    }
                }
            }
            Node value=nd.minimizer();
            nd.setValue(value.getValue());
            return;
        }
        if(nd.getType()=='L'){
            nd.alpha = MAX_VALUE;
            nd.beta = MIN_VALUE;
        }
    }
}
