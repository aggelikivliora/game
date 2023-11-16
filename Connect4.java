package ce326.hw3;
import org.json.*;
import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Scanner;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
@SuppressWarnings("serial")
public class Connect4 extends JFrame{
    //private JPanel panel;
    private myButton[][] buttons = new myButton[6][7];
    private GamePlay gameplay; //arxikopoieitai otan epilex8ei mode (px TRIVIAL)
    private ListPanel usersHistory;
    private PanelPalette palette;
    private PreviousGames currGame;
    private String historyPath;
    public Connect4() {
        setLayout(new BorderLayout());
        initComponents();
        String path= System.getProperty("user.home");
        historyPath = path.concat("/connect4");
        usersHistory = new ListPanel();
        initHistory();
        initHelp();
        yourTurn=0;    // o paikths kanei thn epomenh kinhsh otan yourTurn=1
        start=0;       // ksekina to game
        firstPlayer=0; // proepilegmeno AI = 0
        lastMove= new int[2];
        palette = new PanelPalette();//
        addKeyListener(new KeyboardListener());
        setContentPane(palette);
        palette.setVisible(true);//
    }
    
    //###################### PANELS && LISTENERS ###################################
    @SuppressWarnings("serial")
    public class PanelPalette extends JPanel{
        public PanelPalette(){
            super(new GridLayout(6,7,25,25));
            setBackground(Color.BLUE);
            ButtonListener listener = new ButtonListener();
            for(int i=0; i<6; i++){
                for(int j=0; j<7; j++){
                    myButton temp = new myButton();
                    buttons[i][j]=temp;
                    buttons[i][j].setBackground(Color.lightGray);
                    temp.addMouseListener(listener);
                    add(temp);
                }
            }
        }
    }
    @SuppressWarnings("serial")
    public class KeyboardListener extends JFrame implements KeyListener {
        @Override
        public void keyTyped(KeyEvent e) {
            if(start==1){
                if(yourTurn==1){
                    switch(e.getKeyChar()){
                        case '1' : 
                            throwColor(0);
                            break;
                        case '2' : 
                            throwColor(1);
                            break;
                        case '3' : 
                            throwColor(2);
                            break;
                        case '4' : 
                            throwColor(3);
                            break;
                        case '5' : 
                            throwColor(4);
                            break;
                        case '6' : 
                            throwColor(5);
                            break;
                        case '7' : 
                            throwColor(6);
                            break;
                    }
                }
                if(yourTurn == 0){
                    AIsMove();
                }
            }
        }
        @Override
        public void keyPressed(KeyEvent e) {}
        @Override
        public void keyReleased(KeyEvent e) {}
    }
    
    public class ButtonListener implements MouseListener {
        @Override
        public void mouseClicked(MouseEvent e) {
            if(e.getClickCount()==2){
                int flag=0;
                if(start==1){
                    ////// PLAYERS TURN ///////////////
                    if(yourTurn==1){
                        for(int i=0; i<6; i++){
                            for(int j=0; j<7; j++){
                                if((e.getSource() == buttons[i][j])){
                                    if(buttons[0][j].getBackground()!=Color.lightGray){
                                        flag=1;
                                        break;
                                    }
                                    throwColor(j); // kai AIs move mesa ap thn throwColorkalietai
                                    flag=1;
                                    break;
                                }
                            }
                            if(flag==1){break;} 
                        }
                    }
                }
            }
        }
        @Override
        public void mousePressed(MouseEvent e) {}
        @Override
        public void mouseReleased(MouseEvent e) {}
        @Override
        public void mouseEntered(MouseEvent e) {}
        @Override
        public void mouseExited(MouseEvent e) {}
    }
    @SuppressWarnings("serial")
    public class ListPanel extends JPanel implements ListSelectionListener{
        private DefaultListModel<String> listModel;
        private JList<String> list;
        public void addElement(String str){
            listModel.add(0, str);
        } 
        public ListPanel(){
            super(new BorderLayout());
            listModel = new DefaultListModel<String>(); 
            File directory = new File(historyPath);
            ArrayList<String> files = new ArrayList<>();
            String existingFiles[] = directory.list();
            LocalDateTime Dates[] = new LocalDateTime[existingFiles.length];
            for(int i=0; i<existingFiles.length; i++){
                if(existingFiles[i].equals(".directory")){
                    for(int j=i; i<existingFiles.length-1; i++){
                        existingFiles[j]=existingFiles[j+1];
                        files.add(existingFiles[j+1]);
                    }
                    break;
                }
                else{
                    files.add(existingFiles[i]);
                }
            }
            sort(Dates,files);
            for (int i=0; i<files.size(); i++) {
                if(files.get(i).equals(".directory")){continue;}
                listModel.addElement(files.get(i).substring(0, existingFiles[i].length()-5));
            }
            list = new JList<>(listModel);
            list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            list.addListSelectionListener(this);
            list.addMouseListener(new MouseListener() {//  isws na to pros8esw se ka8e stoixeio ths listas
                public void mouseClicked(MouseEvent evt) {
                    if (evt.getClickCount() == 2) {
                        String s = list.getSelectedValue();
                        String path = historyPath+'/'+s+".json";
                        StringBuilder strBuilder = new StringBuilder();
                        try(Scanner sc2 = new Scanner(new File(path))) {
                            while( sc2.hasNextLine() ) {
                                String string = sc2.nextLine();
                                strBuilder.append(string);
                                strBuilder.append("\n");
                            }
                        } catch(IOException ex) {
                        } catch(IllegalArgumentException ex) {
                        }
                        // // // //CHECK IF THE STRING IS JSON  
                        String jsonString = strBuilder.toString();
                        ArrayList<int[]> moves= new ArrayList<>(); 
                        try {
                            JSONObject myJsonObject=new JSONObject(jsonString);
                            moves=takeArray(myJsonObject);
                        } catch (IllegalArgumentException ex) {
                        }
                        if(s.charAt(33)=='A') {
                            if((moves.size()%2)==0){
                                showGame(moves,'P');
                            }
                            else{
                                showGame(moves,'A');
                            }
                        }
                        else if(s.charAt(33)=='P') {
                            //showGame(moves);
                            if((moves.size()%2)==0){
                                showGame(moves,'A');
                            }
                            else{
                                showGame(moves,'P');
                            }
                        }
                    }
                }
                @Override
                public void mousePressed(MouseEvent e) {}
                @Override
                public void mouseReleased(MouseEvent e) {}
                @Override
                public void mouseEntered(MouseEvent e) {}
                @Override
                public void mouseExited(MouseEvent e) {}
            });
            JScrollPane scr = new JScrollPane(list);
            list.setLayoutOrientation(JList.VERTICAL);
            add(scr,BorderLayout.CENTER);
            add(scr);
            }
        @Override
        public void valueChanged(ListSelectionEvent e) {}
    }
    //#############################################################################################################################
    //ARXIKOPOIHSH KAI TAKSINOMHSH FAKELWN
    public void sort (LocalDateTime Dates[],ArrayList<String> existingFiles){
        for(int i=0; i<existingFiles.size(); i++){
            int day = Integer.parseInt(existingFiles.get(i).substring(8, 10));
            int month = Integer.parseInt(existingFiles.get(i).substring(5, 7));
            int year = Integer.parseInt(existingFiles.get(i).substring(0, 4));
            int hours = Integer.parseInt(existingFiles.get(i).substring(13, 15));
            int minutes = Integer.parseInt(existingFiles.get(i).substring(16, 18));
            LocalDateTime thisDate = LocalDateTime.of(year,month,day,hours,minutes);
            if(i==0){
                Dates[i]=thisDate;
            }
            for(int j=0; j<i; j++){
                if(thisDate.compareTo(Dates[j])>0){
                    String temp = existingFiles.get(i);
                    for(int k=i; k>j; k--){
                        Dates[k] = Dates[k-1];
                        String s = existingFiles.get(k-1);
                        existingFiles.set(k, s);
                    }
                    Dates[j]=thisDate;
                    existingFiles.set(j, temp);
                    break;
                }
                else if(j==(i-1)){Dates[i]=thisDate;}
            }
        }
    }
    
    public ArrayList<int[]> takeArray(JSONObject obj){
        JSONArray arr = obj.getJSONArray("steps");
        ArrayList<int[]> moves = new ArrayList<>();
        for (int i = 0; i < arr.length(); i++){ 
            JSONArray intArr = arr.getJSONObject(i).getJSONArray("child");
            int[] nxt = new int[2];
            nxt[0]=intArr.getJSONObject(0).getInt("x");
            nxt[1]=intArr.getJSONObject(0).getInt("y");
            moves.add(nxt);
        }
        return moves;
    }
    
    //########################### SHOW PREVIOUS GAMES #######################################
    public void showGame(ArrayList<int[]> moves, char first){
        setContentPane(palette);
        resetButtonColor();
        palette.setVisible(true);
        for(int i=0; i<moves.size(); i++){
            if(first=='P'){
                Timer timer1 = new Timer(1000+(i*3000), new myDelayListener(moves.get(i)[0],moves.get(i)[1],first,1));
                timer1.setRepeats(false);
                timer1.start();
                Timer timer = new Timer(3000+(i*3000), new myDelayListener(moves.get(i)[0],moves.get(i)[1],first,0));
                timer.setRepeats(false);
                timer.start();
                first='A';
            }
            else if(first=='A'){
                Timer timer1 = new Timer(1000+(i*3000), new myDelayListener(moves.get(i)[0],moves.get(i)[1],first,1));
                timer1.setRepeats(false);
                timer1.start();
                Timer timer = new Timer(3000+(i*3000), new myDelayListener(moves.get(i)[0],moves.get(i)[1],first,0));
                timer.setRepeats(false);
                timer.start();
                first='P';
            }
        }
    }
    public class myDelayListener implements ActionListener{
        int x;
        int y;
        char turn;
        int flagIntencity;
        public myDelayListener(int x,int y, char turn, int flagIntencity){
            this.x = x;
            this.y = y;
            this.turn = turn;
            this.flagIntencity = flagIntencity;
        }
        @Override
        public void actionPerformed(ActionEvent e) {
            if(start==0){
                if((turn=='P')&&(flagIntencity==0)){
                    buttons[x][y].setBackground(Color.RED);
                }
                else if((turn=='A')&&(flagIntencity==0)){
                    buttons[x][y].setBackground(Color.YELLOW);
                }
                else if((turn=='P')&&(flagIntencity==1)){
                    Color c = new Color(255,102,102);
                    buttons[x][y].setBackground(c);
                }
                else if((turn=='A')&&(flagIntencity==1)){
                    Color c = new Color(255,255,204);
                    buttons[x][y].setBackground(c);
                }
            }
        }
    
    }
    //######################### ARXIKOPOIHSH ISTORIKOU/HELP ###############################
    private void initHelp(){
        Help = new JMenu();
        Help.setText("Help");
        Help.setEnabled(false);
        jMenuBar1.add(Help);
        setJMenuBar(jMenuBar1);
        pack();
        setLocationRelativeTo(null);
    }
    private void initHistory(){
        History = new JMenu();
        History.setText("History");
        History.addMenuListener(new MenuListener(){
            @Override
            public void menuSelected(MenuEvent e) {
                start=0;
                palette.setVisible(false);
                setContentPane(usersHistory);
                usersHistory.setVisible(true);
            }
            @Override
            public void menuDeselected(MenuEvent e) {}
            @Override
            public void menuCanceled(MenuEvent e) {}
        
        });
        jMenuBar1.add(History);
        setJMenuBar(jMenuBar1);
        pack();
        setLocationRelativeTo(null);
    }
    //##################### KINHSH AI, KAI XRMATISMOS KINHSEWN ###########################
    public void throwColor(int j){
        int k;
        for(k=5; k>=0; k--){  // vazw to pouli tou paikth sth swsth 8esh
            if(buttons[k][j].getBackground()==Color.lightGray){
                Color c = new Color(255,102,102);
                buttons[k][j].setBackground(c);
                lastMove[0]=k;
                lastMove[1]=j;
                gameplay.addPlayersPos(k, j);
                break;
            }
        }
        Timer timer = new Timer(1000, new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                buttons[lastMove[0]][lastMove[1]].setBackground(Color.RED);
                start = 1;
                int win=gameplay.win();
                if(win==1){
                    currGame.setWinner("P");
                    //System.out.println(currGame.newGame());
                    String filename = currGame.newGame();
                    usersHistory.addElement(filename);
                    try{
                        gameplay.createFile(historyPath, filename);
                    }
                    catch(IOException ex){
                    
                    }
                    boolean modal=true;
                    winBox my= new winBox(Connect4.this, "You win!",modal,win);
                    resetButtonColor();
                    start=0;
                }
                if(start==1){ AIsMove();}
            }
        });
        timer.setRepeats(false);
        start=0;
        timer.start();
    }
    ///////////////// AIs TURN ////////////////////
    public void AIsMove(){ 
        lastMove = gameplay.nextMove(lastMove);
        Color c = new Color(255,255,204);
        buttons[lastMove[0]][lastMove[1]].setBackground(c);
        gameplay.addAIPos(lastMove[0], lastMove[1]);
        Timer timer = new Timer(1000, new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                buttons[lastMove[0]][lastMove[1]].setBackground(Color.YELLOW);
                start = 1;
                yourTurn=1;
                int win=gameplay.win();
                if(win==0){
                    currGame.setWinner("AI");
                    String filename = currGame.newGame();
                    usersHistory.addElement(filename);
                    try{
                        gameplay.createFile(historyPath, filename);
                    }
                    catch(IOException ex){
                    
                    }
                    boolean modal=true;
                    winBox my= new winBox(Connect4.this, "You lost!",modal,win);
                    resetButtonColor();
                    start=0;
                }
            }
        });
        timer.setRepeats(false);
        start=0;
        timer.start();
    }
    @SuppressWarnings("serial")
    public class winBox extends JDialog{
        public winBox(Connect4 f,String message,boolean modal, int player){
            super(f,"",modal);
            this.setSize(300, 50);
            JPanel panel = new JPanel();
            //if(player==1){
            panel.add(new JLabel(message));
            setLocationRelativeTo(f);
            this.getContentPane().add(panel);
            this.setVisible(true);
        }
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jMenuBar1 = new javax.swing.JMenuBar();
        NewGame = new javax.swing.JMenu();
        trivial = new javax.swing.JMenuItem();
        medium = new javax.swing.JMenuItem();
        Hard = new javax.swing.JMenuItem();
        Player = new javax.swing.JMenu();
        AI = new javax.swing.JRadioButtonMenuItem();
        You = new javax.swing.JRadioButtonMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Connect4");
        getContentPane().setLayout(new java.awt.GridLayout(1, 0));

        NewGame.setText("New Game");

        trivial.setText("Trivial");
        trivial.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                trivialActionPerformed(evt);
            }
        });
        NewGame.add(trivial);

        medium.setText("Medium");
        medium.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mediumActionPerformed(evt);
            }
        });
        NewGame.add(medium);

        Hard.setText("Hard");
        Hard.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                HardActionPerformed(evt);
            }
        });
        NewGame.add(Hard);

        jMenuBar1.add(NewGame);

        Player.setText("1st Player");
        buttonGroup1.add(Player);

        buttonGroup1.add(AI);
        AI.setSelected(true);
        AI.setText("AI");
        AI.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AIActionPerformed(evt);
            }
        });
        Player.add(AI);

        buttonGroup1.add(You);
        You.setText("You");
        You.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                YouActionPerformed(evt);
            }
        });
        Player.add(You);

        jMenuBar1.add(Player);

        setJMenuBar(jMenuBar1);

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    public void resetButtonColor(){
        for(int i=0; i<6;i++){
            for(int j=0; j<7; j++){
                if(buttons[i][j].getBackground()!=Color.lightGray){
                    buttons[i][j].setBackground(Color.lightGray);
                }
            }
        }
        if(gameplay!=null){
            gameplay.reset();
        }
    }
    
//## TRIVIAL MEDIUM HARD  ######################################################
    private void trivialActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_trivialActionPerformed
        start=1;
        gameplay = new GamePlay(1);
        usersHistory.setVisible(false);//
        setContentPane(palette);//
        palette.setVisible(true);//
        //if(start==1){
            resetButtonColor();
        //}
        yourTurn=firstPlayer;
        currGame = new PreviousGames("Trivial");
        if(yourTurn==0){
            AIsMove();
        }
    }//GEN-LAST:event_trivialActionPerformed

    private void mediumActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mediumActionPerformed
        start=1;
        gameplay = new GamePlay(3);
        usersHistory.setVisible(false);//
        setContentPane(palette);//
        palette.setVisible(true);//
        //if(start==1){
            resetButtonColor();
        //}
        yourTurn=firstPlayer;
        currGame = new PreviousGames("Medium");
        if(yourTurn==0){
            AIsMove();
        }
    }//GEN-LAST:event_mediumActionPerformed

    private void HardActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_HardActionPerformed
        start=1;
        gameplay = new GamePlay(5);
        usersHistory.setVisible(false);//
        setContentPane(palette);//
        palette.setVisible(true);//
        //if(start==1){
            resetButtonColor();
        //}
        yourTurn=firstPlayer;
        currGame = new PreviousGames("Hard");
        if(yourTurn==0){
            AIsMove();
        }
    }//GEN-LAST:event_HardActionPerformed
//########################  1H KINHSH  #########################################
    private void AIActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AIActionPerformed
        firstPlayer=0;
    }//GEN-LAST:event_AIActionPerformed

    private void YouActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_YouActionPerformed
        firstPlayer=1;
    }//GEN-LAST:event_YouActionPerformed
    //##############################################################################
    private static void createGUI(){
        Connect4 myPalete= new Connect4();
        myPalete.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        myPalete.pack();
        myPalete.setVisible(true);
        myPalete.setSize(1000,1000);
    }
    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Connect4.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Connect4.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Connect4.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Connect4.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        String path= System.getProperty("user.home");
        String dirPath = path.concat("/connect4");
        File connect4 = new File(dirPath);
        if(!connect4.exists()){
            connect4.mkdir();
        }
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                createGUI();
            }
        });
    }
    ///////////////////////////FLAGS////////////////////////////////////////////
    private int yourTurn;    // o paikths kanei thn epomenh kinhsh otan yourTurn=1
    private int start;       // ksekina to game
    private int firstPlayer; // proepilegmeno AI = 0
    private int[] lastMove;
    ///////////////////////myMENUS//////////////////////////////////////////////
    private JMenu History;
    private JMenu Help;
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JRadioButtonMenuItem AI;
    private javax.swing.JMenuItem Hard;
    private javax.swing.JMenu NewGame;
    private javax.swing.JMenu Player;
    private javax.swing.JRadioButtonMenuItem You;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem medium;
    private javax.swing.JMenuItem trivial;
    // End of variables declaration//GEN-END:variables
}
