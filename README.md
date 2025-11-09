# Zad1 ZTP

Aplikacja umożliwiająca zarządzanie produktami oraz dynamiczną listą zabronionych fraz. Użytkownik może również sprawdzić historię zmian produktów.

## Funkcjonalności

### 1. Podstawowe funkcjonalności (na ocenę 3.0)

- CRUD produktów: tworzenie, odczyt, aktualizacja, usuwanie.
- Walidacja i obsługa błędów wejściowych.

### 2. Forbidden Phrases

Dynamiczna lista zabronionych fraz: uniemożliwia tworzenie produktów, których nazwy zawierają zabronione frazy.

### 3. Historia zmian

Każda modyfikacja produktu (zmiana ceny, ilości, nazwy itp.) jest zapisywana w tabeli product_history z informacją o polach, wartościach przed i po zmianie oraz znaczniku czasu.

## Instalacja

Aby uruchomić aplikację lokalnie, wykonaj poniższe kroki:

### 1. Konfiguracja Bazy Danych

- Należy zainstalować docker oraz narzędzie do zarzadzania bazą danych ( w przypadku tego projektu był to MySQL Workbench)
- Z dockerhub pobrać obraz mysql(terminal gameshop): docker pull mysql
- Aktywacja kontenera: docker run --name zad1-mysql -e MYSQL_ROOT_PASSWORD=password -d -p 3306:3306 mysql

- Należy dodać nowe połączenie w MySQL Workbench
- Hasło do bazy: password
- Dodać schema i wykonać w niej komendy zawarte w pliku backup.sql.

### 2. Instalacja Backend (Spring Boot)

- Wymagana jest instalacja maven i dodanie go do PATH
- Otworzyć terminal główny aplikacji
- Zainstalować Maven w swoim projekcie używając komendy: mvn clean install
- Uruchomić aplikację Spring Boot przy użyciu: mvn spring-boot:run
- testy były wykonywane przy użyciu test.http

## Endpointy

### 1. Endpointy products

- GET /api/v1/products: Pobieranie wszystkich produktów.
- GET /api/v1/products/33: Pobieranie produktu o określonym ID.
- POST /api/v1/products: Tworzenie nowego produktu.
- PUT /api/v1/products/33: Aktualizacja produktu o określonym ID.
- DELETE /api/v1/products/33: Usuwanie produktu o określonym ID

### 2. Endpointy historia oraz foribbden phrases

- GET /api/v1/products/33/history: Pobieranie historii zmian produktu o określonym ID.
- GET /api/v1/forbidden-phrases: Pobieranie listy zabronionych fraz.
- POST /api/v1/forbidden-phrases: Dodawanie nowej zabronionej frazy.
- DELETE /api/v1/forbidden-phrases/5: Usuwanie zabronionej frazy o określonym ID

## Technologie

- Backend: Spring Boot, Spring Data JPA, Java
- Baza danych: MySQL

## Dane logowania do bazy:
- Nazwa użytkownika: root
- Hasło: password

## Autor
- Damian Pasek
