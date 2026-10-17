import java.util.ArrayList;

//HAVA DURUMU UYGULAMASI
public class ObserverPattern {
    public static void main(String[] args){
        WeatherData weatherData=new WeatherData();
        CurrentConditionDisplay mevcutDurum=new CurrentConditionDisplay(weatherData);   //panel tanımlanır ve bağımlı olacağı sınıf belirlenir
        //StatisticsDisplay statisticsDisplay=new StatisticsDisplay(weatherData);
        weatherData.setMeasurements(80,65,30.4f);   //verileri belirlenir (measurementsChanged ve notifyObs ile bağımlı sınıflarda güncellenir)
    }
}

//BAĞIMLI OLUNACAK SINIF BUNU İMPLEMENTE EDECEK   ->   Katılımcı Rol: Subject
interface Subject{
    public void registerObs(Observer o);
    public void removeObs(Observer o);
    public void notifyObs();
}
//BAĞIMLI OLACAK SINIFLAR DEĞER GÜNCELLEMELERİ İÇİN BUNU İMPLEMENTE EDECEK   ->   Katılımcı Rol: Observer
interface Observer{
    public void update(float temp, float humid, float press);
}
//BAĞIMLI OLACAK SINIFLAR GÖRÜNÜM İÇİN BUNU İMPLEMENTE EDECEK
interface DisplayElement{
    public void display();
}

//HAVA DURUMU SINIFI (ÇEŞİTLİ EKRAN PANELLERİ BURAYA BAĞIMLI OLACAK), Subject'i İMPLEMENTE EDER   ->   Katılımcı Rol: ConcreteSubject
class WeatherData implements Subject{
    //bağımlılar(paneller) listesi ve değerler
    private ArrayList<Observer> observers;
    private float temp, humid, press;

    //constructor'da paneller listesi belirlenir
    public WeatherData(){ observers=new ArrayList<>(); }

    //interface metodları Override edilir
    @Override
    public void registerObs(Observer o){ observers.add(o); }    //panel listeye eklenir
    @Override
    public void removeObs(Observer o){ int i=observers.indexOf(o);   if(i>=0){observers.remove(o);} }   //panelin index'i aranır, -1 değilse (listede varsa) çıkarılır
    @Override
    public void notifyObs(){    //tüm paneller tek tek gezilip Observer interface implementasyonuyla değerleri güncellenir
        for(int i=0; i<observers.size(); i++){
            Observer observer=observers.get(i);
            observer.update(temp, humid, press);
        }
    }

    //uygulama tarafından değer güncellemesi isteği geldiğinde değerler güncellenir ve panellere yeni bildiri yapılacağının haberi verilir
    public void measurementsChanged(){ notifyObs(); }
    public void setMeasurements(float temp, float humid, float press){ this.temp=temp; this.humid=humid; this.press=press;   measurementsChanged(); }
}

//ÖRNEK BİR PANEL SINIFI (MEVCUT DURUM PANELİ), GÜNCEL DEĞER VE GÖRÜNÜM İÇİN Observer'ı VE DisplayElement'i İMPLEMENTE EDER   ->   Katılımcı Rol: ConcreteObserver
class CurrentConditionDisplay implements Observer, DisplayElement{
    //kullanılacak değerler belirlenir
    private float temp, humid;
    private Subject weatherData;

    //constructor'da Subject'i implemente eden sınıf alınır ve ona kayıt (bağımlı) yapılır
    public CurrentConditionDisplay(Subject weatherData){ this.weatherData=weatherData;   weatherData.registerObs(this); }

    //interface'lerin metodları Override edilir
    @Override
    public void update(float temp, float humid, float press) { this.temp=temp; this.humid=humid;   display(); }    //notifyObs ile değerler güncellenip görünüm yenilenir
    @Override
    public void display() { System.out.println("Mevcut durumlar: "+temp+"F ve %"+humid+" nem"); }   //görünüm şekli belirlenir
}
/*yeni bir panel sınıfı başka bir şeye dokunmadan kolayca eklenebilir
class StatisticsDisplay implements Observer, DisplayElement{
    //constructor'da Subject örneğine kayıt (bağımlı) yapılır
    public StatisticsDisplay(Subject weatherData){ this.weatherData=weatherData;   weatherData.registerObs(this); }
*/


//SOMUT BAĞIMLILIK YÖNTEMİ (measurementsChanged() içinde tek tek her panel için yeni update satırı)
//Her yeni panel için metodun içine girmek gerekir, değerler runtime'da dinamik olarak güncellenemez

//*** OBSERVER KALIBI ***
//Subject nesnesi ve Observer nesneleri arasında birden-çoğa ilişki vardır. One(Subject) değişince bildirilir ve Many(Observer) otomatik güncellenir

//TASARIM KALIPLARI İLKELERİ
//4. Tasarım İlkesi: Etkileşimli nesneler arasında gevşek bağlı tasarıma uğraş. (Observer nesnesi detayını Subject nesnesi bilmez, sadece genel bir interface'e sahiptir)