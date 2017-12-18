# fast-food-restaurant
Java Design Patterns Homework

Jar indítható.
Indítás: restaurant.Entrance
Java 9-el lett fordítva

Flow:
- Adatok beolvasása .properties fájlokból
- Várólisták létrehozása java.util.concurrent.ArrayBlockingQueue
    - deskQueue - Kiszolgálásra váró ClientGroupok
    - mealQueue - Elkészítésre váró ételek
    - tableQueue - Asztalra váró ClientGroupok
    - cassaQueue -  Fizetésre váró ClientGroupok
- ClientGenerator létrehozása és indítása
- Megadott számú Desk létrehozása és indítása
- Megadott számú Chef létrehozása és indítása
- Megadott számú, és tartományon belül véletlenszerű székkel rendelkező Table létrehozása
- TableService létrehozása és indítása
- Cassa létrehozása és indítása

ClientGroup flow:
- Legenerálódik
- Pénztárhoz megy
- Leadja a rendelést
    - Desk átveszi a rendelést
    - Chef elkészíti a rendelést
    - Desk tovább küldi
- Asztalt keres
    - TableService kiutalja az asztalt
- Elindítja a Clienteket
    - Clientek megeszik az ételt
- Kasszához megy
- Fizet és távozik
  
Osztályok:
- restaurant.Entrance
    - Alkalmazás indítása
    - Inicializálás
restaurant.Restaurant
    - Service-k indítása
- restaurant.service.ClientGenerator
    - Tartományon belül véletlenszerű időközönként legenerál egy ClientGroupot, amit berak a deskQueue-ba
- restaurant.client.ClientGroup
    - Egységként kezeli a létrehozáskor megadott számú Clientet
    - Asztalhoz ülést követően elindítja a hozzá tartozó Clienteket
    - Vár, amíg az összes Client elfogyasztja az ételét
    - "Beáll a kasszához"
- restaurant.client.Client
    - Tárolja az adott "Személyre" vonatkozó adatokat
    - Indítás után "elfogyasztja az ételeit"
- restaurant.service.Desk
    - Fogadja a ClientGroupokat
    - Felveszi az adott ClientGrouphoz tartozó Clientek rendelését
    - Rendelést berakja a mealQueue-ba
    - Megvárja, amíg a Chefek elkészítik az adott ClientGrouphoz tartozó ételt
    - A ClientGroupot berakja a tableQueue-ba
- restaurant.service.Chef
    - Kiveszi a következő elemet a mealQueue-ból
    - Az adott ételre jellemző idő alatt elkészíti az ételt.
- restaurant.service.Table
    - Meghatározza, hogy legfeljebb mekkora ClientGroup "ülhet le hozzá"
- restaurant.service.TableService
    - Folyamatosan figyeli a tableQueue-ban várakozó ClientGroup-okat, és az üres asztalokat.
    - Amennyiben van olyan üres asztal, "amiben elfér a ClientGroup", "leülteti a ClientGroupot"
    - Elindítja a ClientGroupot
- restaurant.service.Cassa
    - Kiveszi a ClientGroup-okat a cassaQueue-ból, és megadott tartományon belül véletlenszerű idő alatt "kifizetteti a számlát"
    - Elindítja a leállítási folyamatot, ha az összes Client elhagyta az üzletet
- restaurant.meals.FoodType
    - Tárolja egy bizonyos MainCourse vagy Extra adatait.
- restaurant.meals.Food
    - Véletlenszerű MainCourse létrehozása
    - Véletlenszerű Extra lista létrehozása
    - Tárolja az adott Food állapotát
- restaurant.meals.Meal
    - Véletlenszerű Meal lista létrehozása
    - Tárolja az adott fogáshoz tartozó MainCourse-t és Extrákat
    - Beállítja a tárolt Foodok állapotát
    - Növeli az őt elfogyasztó Client morálját
- restaurant.util.Logger
    - Kiírja a kapott üzeneteket
    - A kiírás nem valós idejű
    - System.out a részletes, System.err a fontos dolgokat jeleníti meg.
- restaurant.util.Random
    - Különféle véletlenszerű értékeket szolgáltat
- restaurant.util.TableSortingComparator
    - TableService használja a Table-k férőhely szerinti sorba rendezéséhez.
    - Cél: A ClientGroupok méretének leginkább megfelelő asztal lefoglalása
    restaurant.util.FoodComparator
        - Moralváltoztató érték alapján sorba rendezi a Foodokat
- restaurant.util.Logout
    - Bezárja az alkalmazást
    - Kilogolja a statisztikát
- restaurant.util.PropLoader
    - Beolvassa .properties fájlokból az étterem adatait
    - Elkészíti a felszolgálható MainCourse-ok és Extrák listáját
    - Hozzáférést biztosít a beolvasott értékeknek
    
