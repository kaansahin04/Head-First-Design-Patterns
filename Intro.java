//ÖRDEK UYGULAMASI
public class Intro {
    public static void main(String[] args){
        Duck mallard=new MallardDuck();     //yeşilbaş türünde ördek tanımlanır, davranışları constructor'da atanır
        mallard.performFly();           //atanan uçma davranışı tetiklenir
        mallard.performQuack();         //atanan vakvaklama davranışı tetiklenir
        mallard.setQuackBehavior(new Squeak());     //vakvaklama davranışını değiştiriyoruz
        mallard.performQuack();
    }
}


//ORTAK ÖRDEK SINIFI   ->   Katılımcı Rol: Context
abstract class Duck{
    //davranışlar için genel değişkenler (davranış aileleri) belirlenir, tanımları subclass'larda yapılır ve işler bu Strategy nesnelerine delege edilir
    FlyBehavior flyBehavior;
    QuackBehavior quackBehavior;

    //constructor
    public Duck(){}

    //herkeste ortak olan yüzme metodu
    public void swim(){ System.out.println("Yüzüyorum."); }

    //miras alacaklar için görünüm metodu
    public abstract void display();         //her ördek türünün görünümü farklı olacağı için "abstract" olarak tanımlanır, görünüm oralarda belirlenir

    //davranış metodları
    public void performFly(){ flyBehavior.fly(); }              //hâli belirlenen uçma davranışının metodu çalışır
    public void performQuack(){ quackBehavior.quack(); }        //hâli belirlenen vakvaklama davranışının metodu çalışır

    //davranışı sonradan değişecekler için setter metodları
    public void setFlyBehavior(FlyBehavior fb){ flyBehavior=fb; }
    public void setQuackBehavior(QuackBehavior qb){ quackBehavior=qb; }
}

//YEŞİLBAŞ TÜRÜ ÖRDEK
class MallardDuck extends Duck{
    //constructor, davranışlar tanımlanır
    public MallardDuck(){
        flyBehavior=new FlyWithWings();         //kanatlarıyla uçar
        quackBehavior=new Quack();              //aşırı vakvaklar
    }

    //"soyut" görünüm metodu override edilir
    @Override
    public void display(){ System.out.println("Yeşil bir başa, siyah bir kuyruğa ve sarı bir gagaya sahibim."); }
}


//UÇMA DAVRANIŞI   ->   Katılımcı Rol: Strategy
interface FlyBehavior{ public void fly(); }         //genel uçma davranışı "interface" olarak tanımlanır ve metodlar belirlenir
//UÇMA DAVRANIŞININ ÇEŞİTLİ HÂLLERİ (somut sınıflar)   ->   Katılımcı Rol: ConcreteStrategy
class FlyWithWings implements FlyBehavior{          //çeşitli uçma davranışları, genel davranışı implemente eder ve metod şekillendirilir
    public void fly(){ System.out.println("Kanatlarımla uçuyorum."); }
}
class FlyNoWay implements FlyBehavior{
    public void fly(){ System.out.println("Uçamıyorum."); }
}
/*yeni bir uçma davranışı başka bir şeye dokunmadan kolayca eklenebilir
class FlyRocketPowered implements FlyBehavior{
    public void fly(){ System.out.println("Roketle uçuyorum."); }
}
*/

//VAKVAKLAMA DAVRANIŞI   ->   Katılımcı Rol: Strategy
interface QuackBehavior{ public void quack(); }         //genel vakvaklama davranışı "interface" olarak tanımlanır ve metodlar belirlenir
//VAKVAKLAMA DAVRANIŞININ ÇEŞİTLİ HÂLLERİ (somut sınıflar)   ->   Katılımcı Rol: ConcreteStrategy
class Quack implements QuackBehavior{          //çeşitli vakvaklama davranışları, genel davranışı implemente eder ve metod şekillendirilir
    public void quack(){ System.out.println("Aşırı vakvaklıyorum."); }
}
class Squeak implements QuackBehavior{
    public void quack(){ System.out.println("Gıcırdıyorum/Cikliyorum."); }
}
class Mute implements QuackBehavior{
    public void quack(){ System.out.println("Vakvaklamıyorum."); }
}


//MİRAS YÖNTEMİ (Superclass: Duck, Subclasses: MallardDuck, RubberDuck vb.)
//Sonradan uçma özelliği eklenmek istendiğinde Duck'a fly tanımlanır ve her ördek uçar ama RubberDuck normalde uçamaz.

//ARAYÜZ YÖNTEMİ (Bir Flyable arayüzü olur, uçabilen ördekler bunu implemente eder)
//Uçan her ördek için tek tek fly metodu tanımlanır, bir değişiklik geldiğinde hepsinin güncellenmesi gerekir

//*** STRATEGY KALIBI ***
//Algoritma ailesi tanımlanır, algoritmalar (davranışlar) kapsüllenir ve runtime'da birbirlerinin yerine kullanılabilir hâle gelir.

//TASARIM KALIPLARI İLKELERİ
//1. Tasarım İlkesi: Değişen yönleri bul, aynı kalan yönlerden ayır. (Güncelleme alacak davranışlar superclass'tan dışarı alınır, sadece o parça güncellenir)
//2. Tasarım İlkesi: Implementasyona göre değil, arayüze göre programla. (Kodun mantığı, somut sınıf nesnelerine değil arayüz kurallarına bağımlı kılınır)
//3. Tasarım İlkesi: Miras almak yerine kompozisyon kullan. (IS-A (yeni özellik için başka sınıftan türeme) yerine HAS-A (o sınıfta başka yetenek nesnesi) kullanılır)
//GENEL KURAL
//Open/Closed Principle: Gelişmeye Açık, Değişime Kapalı. Mevcut kod üzerinde mümkün olduğunca az değişiklik yapılmalıdır.