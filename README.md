1. mvn clean package -DskipTests
2. docker volume create mysql_coo
3. docker compose up -d --build
4. Зайти в свагер http://localhost:8080/swagger-ui/index.html#/Recipe/getAllRecipes
згенерувати jwt token -> api/v1/auth/generate username: "admin", password: "password", email: "email.com"
5. вписати код в authoriz після чого будуть доступні всі контрольні точки
6. спочатку потрібно створити recipe через свагер можно метод POSt
7. потом можеш створити Event POST вписуй в userId: 1, recipeId: 1 -> це для коректної роботи
8. ну і все, дальше все залекжить від тебе :)
