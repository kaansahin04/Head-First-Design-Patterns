//PİZZA DÜKKÂNI UYGULAMASI
public class FactoryPattern {
    public static void main(String[] args) {
        PizzaStore nyStore=new NYPizzaStore();    //hangi somut pizzanın üretileceğini bilmeden sadece fabrika üzerinden sipariş verilir
        PizzaStore chicagoStore=new ChicagoPizzaStore();

        Pizza pizza=nyStore.orderPizza("cheese");     //NY şubesinden peynirli pizza siparişi: üretim alt sınıfa (NYPizzaStore) delege edilecek
        System.out.println(pizza.getName()+" sipariş edildi. \n");

        Pizza pizza2=chicagoStore.orderPizza("cheese");    //aynı sipariş ama farklı bölge
        System.out.println(pizza2.getName()+" sipariş edildi. \n");
    }
}

//ORTAK PİZZA SINIFI   ->   Katılımcı Rol: Product
abstract class Pizza {
    String name;
    String hamur;
    String sos;

    //ismin get edilmesi
    public String getName() { return name; }

    //tüm pizzalarda ortak olan hazırlık adımları
    public void prepare() {
        System.out.println("Hazırlanan pizza: "+name);
        System.out.println("Çevrilen hamur: "+hamur);
        System.out.println("Eklenen sos: "+sos);
    }

    public void bake() { System.out.println("350 derecede 25 dk. pişirme"); }
    public void cut() { System.out.println("Çapraz dilimleme"); }
    public void box() { System.out.println("Paketleme"); }
}

//ÇEŞİTLİ SOMUT ÜRÜN SINIFLARI   ->   Katılımcı Rol: ConcreteProduct
class NYStyleCheesePizza extends Pizza {
    public NYStyleCheesePizza() {
        name="NY Style Sos ve Peynirli Pizza";
        hamur="İnce Hamur";
        sos="Marinara Sos";
    }
}
/*yeni bir pizza çeşidi başka bir şeye dokunmadan kolayca eklenebilir
class NYStyleMushroomPizza extends Pizza {
    public NYStyleMushroomPizza() {
        name="NY Style Mantarlı Pizza";
        hamur="Mayasız Hamur";
        sos="Sarımsaklı Krema Sosu";
    }
}
*/

class ChicagoStyleCheesePizza extends Pizza {
    public ChicagoStyleCheesePizza() {
        name="Chicago Style Deep Dish Peynirli Pizza";
        hamur="Extra Kalın Hamur";
        sos="Domates Sosu";
    }

    //ortak dilimleme adımı alt sınıfta özelleştirilebilir
    @Override
    public void cut() { System.out.println("Kare dilimleme"); }
}

//ORTAK SOYUT ÜRETİCİ MAĞAZA SINIFI   ->   Katılımcı Rol: Creator
abstract class PizzaStore {
    public Pizza orderPizza(String tip) {      //tipi belirlenen (peynirli gibi) pizzanın hazırlandığı yer
        Pizza pizza = createPizza(tip);    //üretme metodu çağrılır, hangi somut sınıfın döneceğine alt sınıf karar verir

        //hazırlanma aşamaları
        pizza.prepare();
        pizza.bake();
        pizza.cut();
        pizza.box();

        return pizza;   //hazırlanan pizzayı sonuç olarak döndür
    }

    protected abstract Pizza createPizza(String tip);      //main'de alt sınıflardan nesne oluşturulacak
}

//ÇEŞİTLİ SOMUT ÜRETİCİ MAĞAZA SINIFLARI   ->   Katılımcı Rol: ConcreteCreator
class NYPizzaStore extends PizzaStore {
    @Override
    protected Pizza createPizza(String malzeme) {
        if (malzeme.equals("cheese")) {
            return new NYStyleCheesePizza();
        }
        /*else if (malzeme.equals("mushroom"){
            return new NYStyleMushroomPizza();
        }
         */
        return null;
    }
}

class ChicagoPizzaStore extends PizzaStore {
    @Override
    protected Pizza createPizza(String malzeme) {
        if (malzeme.equals("cheese")) {
            return new ChicagoStyleCheesePizza();
        }
        return null;
    }
}

//DOĞRUDAN "new" KULLANIM YÖNTEMİ (orderPizza içinde doğrudan somut sınıfları new'lemek)
//Yeni bir pizza çeşidi eklendiğinde veya mevcut çeşit değiştiğinde orderPizza metodu doğrudan değişime açık hâle gelir
//Somut sınıflara sıkı bağlılık oluşur

//BASİT FABRİKA (Simple Factory) YAKLAŞIMI (Ayrı bir SimplePizzaFactory sınıfı)
//Nesne üretimi orderPizza dışına çıkarılıp tek bir yerde toplanır ama farklı bölge kuralları için esneklik sınırlıdır.

//*** FACTORY METHOD KALIBI ***
//Nesne oluşturma soyut şekilde yapılır, ancak hangi sınıfın örnekleneceğine alt sınıflar karar verir.

//TASARIM KALIPLARI İLKELERİ
//6. Tasarım İlkesi: Soyutlamalara bağlı ol, somut sınıflara değil. (Ortak üretici(PizzaStore), somut ürünlere(NYStyleCheesePizza) değil, ortak şablona(Pizza) bağlı olsun.