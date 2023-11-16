package ce326.hw3;
import java.time.LocalDate;
import java.time.LocalTime;

public class PreviousGames {
    LocalDate date;
    LocalTime time;
    String level;
    String winner;
    public PreviousGames(String level){
        date = LocalDate.now();
        time = LocalTime.now();
        this.level = level;
    }
    public String newGame(){
        StringBuilder filename = new StringBuilder();
        filename.append(date.getYear());
        filename.append('.');
        if(date.getMonthValue()<10){filename.append('0');}
        filename.append(date.getMonthValue());
        filename.append('.');
        if(date.getDayOfMonth()<10){filename.append('0');}
        filename.append(date.getDayOfMonth());
        filename.append(" - ");
        if(time.getHour()<10){filename.append('0');}
        filename.append(time.getHour());
        filename.append(':');
        if(time.getMinute()<10){filename.append('0');}
        filename.append(time.getMinute());
        filename.append(" L: ");
        filename.append(level);
        switch (level) {
            case "Hard":
                filename.append("    W: ");
                break;
            case "Trivial":
                filename.append(" W: ");
                break;
            case "Medium":
                filename.append("  W: ");
                break;
        }
        //filename.append(winner);
        if("P".equals(winner)){
            filename.append(winner);
            filename.append(" ");
        }
        else{
            filename.append(winner);
        }
     return filename.toString();
    }
    
    public void setWinner(String winner){
        this.winner =winner;
    }
    public String getWinner(){
        return this.winner;
    }
    public void setDate(LocalDate date){
        this.date =date;
    }
    public LocalDate getDate(){
        return this.date;
    }
    public void setTime(LocalTime time){
        this.time =time;
    }
    public LocalTime getTime(){
        return this.time;
    }
    public void setLevel(String level){
        this.level =level;
    }
    public String getLevel(){
        return this.level;
    }
}
