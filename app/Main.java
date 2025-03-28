import java.util.Scanner;
class Somme {
    private int n1;
    private int n2;

    public Somme(int n1, int n2) {
        this.n1 = n1;
        this.n2 = n2;
    }

    public int som() {
        return n1 + n2;
    }
}

public class Main{
public static void main(String[] args){
Scanner sc = new Scanner(System.in);

System.out.println("entrer un entier a svp");
int a = sc.nextInt();

System.out.println("entrer un entier b svp");
int b = sc.nextInt();

Somme som = new Somme(a, b);

System.out.println("La somme des entiers est : "+som);

}
}