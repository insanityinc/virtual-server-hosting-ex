package Client;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class Menu {

    private List<String> options;
    private int op;

    public Menu(String[] options){
        this.options = new ArrayList<String>();
        for (String op : options)
            this.options.add(op);
    }

    public void run(String nome){
        do {
            showMenu(nome);
            this.op = readOp();
        } while (this.op == -1);
    }

    private void showMenu(String email) {
        System.out.println("\n-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-\n" + "|   Virtual Server Hosting    |\n" + "-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-\n");
        if(email != null)
            System.out.println("Logged in as -> " + "'"+ email + "'\n");
        for (int i=0; i<this.options.size(); i++){
                System.out.print(i+1);
                System.out.print(" - ");
                System.out.println(this.options.get(i));
        }

        System.out.println("0 - Exit");
    }

    private int readOp(){
        int op;
        Scanner s = new Scanner(System.in);
        System.out.print("> ");

        if (!s.hasNextInt()) s.next();
        op = s.nextInt();

        if (op<0 || op>this.options.size()){
            System.out.println("Invalid option");
            op=-1;
        }
        return op;
    }

    public int getOp(){ return this.op; }
}

