Steps to Run in IntelliJ

1. Open the project:                                                                              
   File → Open → Select: /Users/jasonhession/Documents/SEM2_YEAR4/CS4135_Software_Design_and_Architec
   ture/Project/campus2connect/services/auth-service

2. Start PostgreSQL (required first):                                                             
   cd /Users/jasonhession/Documents/SEM2_YEAR4/CS4135_Software_Design_and_Architecture/Project/campus
   2connect/services/auth-service                                                                    
   docker-compose up auth-db -d   ////   docker compose up auth-db -d (if using desktop)

This starts just the database container on port 5432.

3. In IntelliJ:
- Wait for Maven to import dependencies
- Open AuthServiceApplication.java
- Click the green "Run" button next to the main method
- Or right-click → Run 'AuthServiceApplication'

4. Set the dev profile (optional but recommended for admin seeding):
- Run → Edit Configurations
- Add VM option: -Dspring.profiles.active=dev(if you want to test the admin endpoints (/admin/companies/{id}/approve,                      
  /admin/users/{id}/suspend, etc.))

Once running:
- API: http://localhost:8081
- Swagger UI: http://localhost:8081/swagger-ui.html