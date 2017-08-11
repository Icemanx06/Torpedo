package shell;


import javafx.animation.Animation;
import shell.Shell;
import shell.Main;
import shell.Command;
import shell.Result;
import shell.Status;


public class Myshell extends Shell{
    boolean fut = false;
    int raketa = 0;
    Status[][] tabla = null;
    int[] hajok = null;
    
    public void initHajok(){
        hajok = new int[7];
        hajok[0] = 2;
        hajok[1] = 2;
        hajok[2] = 1;
        hajok[3] = 1;
        hajok[4] = 1;
    }
    
    public void setRaketa(int raketa){
        this.raketa = raketa;
    }
    
    public void initTable(){
        tabla = new Status[10][10];
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                tabla[i][j] = Status.WATER;
            }
        }
    }
    
    public void kirajzol(){
        System.out.println("  |A|B|C|D|E|F|G|H|I|J|");
        for (int i = 0; i < 10; i++) {
            int sorszam = i+1;
            if(sorszam == 10){
                System.out.print(sorszam+"|"); 
            }else{
                System.out.print(" "+sorszam+"|");
            }
           
            for (int j = 0; j < 10; j++) {
                if(tabla[i][j] == Status.WATER){
                    System.out.print("~|");
                }else if(tabla[i][j] == Status.HIT){
                    System.out.print("O|");
                }else if(tabla[i][j] == Status.SHIP){
                    System.out.print("X|");
                }else{
                    System.out.print("#|");
                }
               
            }
             System.out.println("");
        }
    }
    
    public void printRaketa(){
        if(raketa != 0){
            System.out.println("Rakétáid száma: "+raketa+"db");
        }else{
            System.out.println("Nincs több rakétád!");
        }
    }
    public void printHajók(){
        System.out.println("Keresendő hajók:");
        for (int i = 0; i < 5; i++) {
            int db = hajok[i];
            if(db != 0){
                switch(i){
                    case 0:
                        System.out.println(db+"db repülő van még!");
                        break;
                    case 1:
                        System.out.println(db+"db naszád van még!");
                        break;
                    case 2:
                        System.out.println(db+"db tengeralatjáró van még!");
                        break;
                    case 3:
                        System.out.println(db+"db romboló van még!");
                        break;  
                    case 4:
                        System.out.println(db+"db anyahajó van még!");
                        break;     
                }
            }
        }
        
    }
    
    public void hajokTorol(int meret){
        switch (meret){
            case 1:
                hajok[0] -= 1;        
                break;
            case 2:
                hajok[1] -= 1;   
                break;
            case 3:
                hajok[2] -= 1;   
                break;
            case 4:
                hajok[3] -= 1;   
                break;  
            case 5:
                hajok[4] -= 1;   
                break;    
        }
    }
    
    public boolean Win(){
      if(hajok[0] == 0 && hajok[1] == 0 && hajok[2] == 0 && hajok[3] == 0 && hajok[4] == 0){
          fut = false;
          return true;
      }
      return false;
    }
    
    public boolean Lose(){
        if(raketa == 0){
            fut = false;
            return true;
        }
        return false;
    }

    @Override
    protected void init() {
        super.init(); 
        setRaketa(15);
        initHajok();
        initTable();
    }

    public Myshell() {
        init();
        
        addCommand(new Command("new") {
            @Override
            public boolean execute(String... args) {
                if(args.length == 0){
                    setRaketa(15);
                    initHajok();
                    initTable();
                    kirajzol();
                    printRaketa();
                    printHajók();
                    fut = true;
                    return true;
                }else if(args.length ==1){
                    String raketSz = args[0];
                    for (int i = 0; i < raketSz.length(); i++) {
                        if(!Character.isDigit(raketSz.charAt(i))){
                            System.out.println("Nem számot adtál meg!");
                            return false;
                        }
                    }
                    setRaketa(Integer.parseInt(raketSz));
                    initHajok();
                    initTable();
                    kirajzol();
                    printRaketa();
                    printHajók();
                    fut = true;
                    return true;
                }
            return false;    
            }
        });
    
        addCommand(new Command("print") {
            @Override
            public boolean execute(String... args) {
                if(args.length == 0 && fut){
                    if(Win()){
                        kirajzol();
                        System.out.println("Nyertél!");
                        fut = false;
                        return true;
                    }else if (Lose()){
                        kirajzol();
                        System.out.println("Vesztettél!");
                        fut = false;
                        return true;
                    }else{
                    kirajzol();
                    printRaketa();
                    printHajók();
                    return true;
                    }
                }else if(args.length > 0){
                    System.out.println("Nem adhatsz meg ehez a parancshoz paramétert!");
                    return false;
                }
                
                System.out.println("Nincs játék folyamatban!");
                return false;
            }
        });
        
        
        addCommand(new Command("fire") {
            @Override
            public boolean execute(String... args) {
                if(fut){
                    if(args.length == 2){
                        if(args[0].length() == 1){
                            if(args[0].charAt(0)<'A' || args[0].charAt(0)>'J'){
                                System.out.println("Nincs ilyen oszlop!");
                                return false;
                            }else{
                                int oszlop = args[0].charAt(0)-65;  
                                for (int i = 0; i < args[1].length(); i++) {
                                    if(!Character.isDigit(args[1].charAt(i))){
                                        System.out.println("Nem számost adtál meg!");
                                        return false;
                                    }
                                }
                                int sor = Integer.parseInt(args[1])-1;
                                if(sor<0 || sor>9){
                                    System.out.println("Nincs ilyen sor!");
                                    return false;
                                }else{
                                    if(tabla[sor][oszlop] != Status.WATER){
                                        System.out.println("Lőttél már ide!");
                                        return false;
                                    }else{
                                        raketa--;
                                        tabla[sor][oszlop] = Status.HIT;
                                        Result result = resultOfShot(sor, oszlop);
                                        boolean talalt = result.isHit();
                                        if(talalt){
                                            raketa +=2;
                                            tabla[sor][oszlop] = Status.SHIP;
                                            boolean sullyedt = result.isSank();
                                            if(sullyedt){
                                                int col = result.getColumn();
                                                int row = result.getRow();
                                                boolean horizontal = result.isHorizontal();
                                                int hajoMerete = result.getSize();
                                                raketa += hajoMerete;
                                                if(horizontal){
                                                    for (int i = col; i < col+hajoMerete; i++) {
                                                        tabla[row][i] = Status.SANK;
                                                    }
                                                }else{
                                                    for (int i = row; i < row+hajoMerete; i++) {
                                                         tabla[i][col] = Status.SANK;
                                                    }
                                                }
                                                hajokTorol(hajoMerete);
                                               
                                                if(Win()){
                                                    kirajzol();
                                                    System.out.println("Talált, süllyedt!");
                                                    System.out.println("Nyertél!");
                                                    return true;
                                                }else{
                                                    kirajzol();
                                                    System.out.println("Talált, süllyedt!");
                                                    return true;
                                                }
                                                
                                            }else{
                                                kirajzol();
                                                System.out.println("Talált!");
                                                return true;
                                            }
                                        }else{
                                            if(Lose()){
                                                kirajzol();
                                                System.out.println("Nem talált!");
                                                System.out.println("Vesztettél!");
                                            }else{
                                              kirajzol();
                                               System.out.println("Nem talált!");  
                                            }
                                            return true;
                                        }
                                    }
                                }
                            }
                        }else{
                            System.out.println("Az első paraméter csak egy karakter lehet!");
                            return false;
                        }
                    }else{
                        System.out.println("Pontosan két paramétert kell megadni!");
                        return false;
                    }
                }
                System.out.println("Nincs futó játék!");
                return false;
            }
        });
    }
    
    
}
