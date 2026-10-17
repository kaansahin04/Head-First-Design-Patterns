//KAHVE SİPARİŞİ UYGULAMASI
public class DecoratorPattern {
    public static void main(String[] args){
        Beverage beverage=new Espresso();   //içecek Espresso olarak belirlendi
        System.out.println(beverage.getAciklama()+"; "+beverage.cost()+" ₺");   //içeceğin bilgileri alındı

        Beverage beverage2=new HouseBlend();    //bir başka içecek
        beverage2=new Mocha(beverage2);     //mocha kattık (wrap)
        beverage2=new Mocha(beverage2);     //double mocha istediğimiz için bir tane daha kattık (wrap)
        beverage2=new SteamedMilk(beverage2);   //süt kattık (wrap)
        System.out.println(beverage2.getAciklama()+"; "+beverage2.cost()+" ₺");   //yeni içeceğin bilgileri

        Beverage beverage3=new Mocha(new SteamedMilk(new SteamedMilk(new Espresso())));     //sarmalayıcı şekilde ayrı bir içecek
        System.out.println(beverage3.getAciklama()+"; "+beverage3.cost()+" ₺");
    }
}

//ORTAK İÇECEK SINIFI   ->   Katılımcı Rol: Component
abstract class Beverage{
    //içeceğin açıklaması ve get edilmesi tanımlanır
    String aciklama="Bilinmeyen içecek";
    public String getAciklama(){ return aciklama; }

    //herkesin ayrı ayrı kullanacağı fiyat metodu
    public abstract double cost();

    //BOYUT ÇEŞİTLERİ İÇİN UYGULANABİLECEK YÖNTEM
    //boyut çeşitleri tanımlanır
    String size;
    public String getSize(){ return size; }
    public void setSize(String size){ this.size=size; }
}

//ORTAK ÇEŞNİ SINIFI (AYRI BİR İÇECEK ÇEŞİDİ TANIMLAYACAĞI İÇİN AYRI FİYATLANDIRMA VE AÇIKLAMALARA SAHİP OLACAKLAR)   ->   Katılımcı Rol: Decorator
abstract class CondimentDecorator extends Beverage{
    //her çeşninin açıklaması ayrı olacak
    public abstract String getAciklama();
}

//AYRI AYRI İÇECEK ÇEŞİTLERİ (SOMUT SINIFLAR)   ->   Katılımcı Rol: ConcreteComponent
class Espresso extends Beverage{
    //constructor'da her içecek kendi açıklamasına sahip olur
    public Espresso(){ aciklama="Espresso"; }

    //"soyut" fiyat metodu override edilir
    @Override
    public double cost(){ return 1.99; }

}
class HouseBlend extends Beverage{
    //constructor'da her içecek kendi açıklamasına sahip olur
    public HouseBlend(){ aciklama="HouseBlend"; }

    //"soyut" fiyat metodu override edilir
    @Override
    public double cost(){ return 0.89; }
}
/*yeni bir içecek çeşidi başka bir şeye dokunmadan kolayca eklenebilir
class DarkRoast extends Beverage{
    public DarkRoast(){ aciklama="DarkRoast"; }
    @Override
    public double cost(){ return 0.99; }
}
*/

//AYRI AYRI ÇEŞNİ ÇEŞİTLERİ (SOMUT SINIFLAR)   ->   Katılımcı Rol: ConcreteDecorator
class Mocha extends CondimentDecorator{
    //çeşni, bir içecek çeşidi için tanımlanır
    Beverage beverage;
    public Mocha(Beverage beverage){ this.beverage=beverage; }

    //o içecek için "soyut" açıklama ve fiyat metodları override edilir
    @Override
    public String getAciklama(){ return beverage.getAciklama()+", Mocha"; }

    //BOYUT ÇEŞİTLERİ İÇİN UYGULANABİLECEK YÖNTEM
    public String getSize(){ return beverage.getSize(); }

    @Override
    public double cost(){
        double cost=beverage.cost();
        if(beverage.getSize().equals("Tall")){cost+=0.15;}
        else if(beverage.getSize().equals("Grande")){cost+=0.20;}
        else if(beverage.getSize().equals("Venti")){cost+=0.25;}
        return cost;
    }
}
class SteamedMilk extends CondimentDecorator{
    Beverage beverage;
    public SteamedMilk(Beverage beverage){ this.beverage=beverage; }

    @Override
    public String getAciklama(){ return beverage.getAciklama()+", SteamedMilk"; }

    //BOYUT ÇEŞİTLERİ İÇİN UYGULANABİLECEK YÖNTEM
    public String getSize(){ return beverage.getSize(); }

    @Override
    public double cost(){
        double cost=beverage.cost();
        if(beverage.getSize().equals("Tall")){cost+=0.05;}
        else if(beverage.getSize().equals("Grande")){cost+=0.10;}
        else if(beverage.getSize().equals("Venti")){cost+=0.15;}
        return cost;
    }
}
/*yeni bir çeşni çeşidi başka bir şeye dokunmadan kolayca eklenebilir
class Whip extends CondimentDecorator{
    Beverage beverage;
    public SteamedMilk(Beverage beverage){ this.beverage=beverage; }
    @Override
    public String getAciklama(){ return beverage.getAciklama()+", Whip"; }
    public String getSize(){ return beverage.getSize(); }
    @Override
    public double cost(){
        double cost=beverage.cost();
        if(beverage.getSize().equals("Tall")){cost+=0.05;}
        else if(beverage.getSize().equals("Grande")){cost+=0.10;}
        else if(beverage.getSize().equals("Venti")){cost+=0.15;}
        return cost;
    }
}
*/


//SUBCLASS YÖNTEMİ (her içecek ve çeşni için ayrı sınıflar üretilir (EspressoWhip, EspressoMochaSoy, EspressoDoubleMochaSoy vb.)
//class explosion (sınıf patlaması) yaşanır, bir sürü sınıf olur
//fiyat değiştiğinde bir sürü sınıf tek tek güncellenmesi gerekir

//*** DECORATOR KALIBI ***
//Nesnelere dinamik olarak (runtime'da) özellik eklenir. Altsınıf olayına esnek bir alternatif sunar. İçecek (component), çeşnilerin (decorator'ların) içinde sarmalanır.
//Birden fazla özelliğin aynı anda istenildiği kadar eklenebilmesi sağlanır. Boyut çeşitleri aynı anda bulunamayacağından SizeDecorator açmak mantığa ters düşer.

//TASARIM KALIPLARI İLKELERİ
//5. Tasarım İlkesi: Sınıflar genişletilmeye açık, modifiye edilmeye kapalı olmalı. (Mevcut kodlarda hiçbir değişiklik yapılmamalı, sadece yeni sınıf eklenebilmeli.)