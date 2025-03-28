Uruchomienie projektu: 

Sposób nr.1 - bez dostępu do bazy danych mongoDB gdzie zapisywana jest historia obliczeń użytkownika, zakłada posiadanie JDK 17 oraz Angulara 15 do uruchomienia
Front-end - cd src/main/frontend; ng serve;
Back-end - mvn clean install -DskipTests; mvn spring-boot:run 

Sposób nr.2 - z dostępem do bazy danych, wymaga posiadania Dockera i uruchomienia komendy 'docker-compose up --build' w katalogu głównym.

Niezależnie od wybranego sposobu, wejście na stronę: localhost:4200
