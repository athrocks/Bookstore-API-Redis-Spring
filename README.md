
## README: Using Postman for API Testing with Redis and Spring Boot

### **Prerequisites**
1. **Postman**: Installed on your machine.
2. **Docker and Docker Compose**: Properly installed and configured.
3. **Redis CLI**: Available in your system for monitoring Redis operations.
4. **Spring Boot Application**: The backend API should be configured to interact with Redis.

---

### **Steps to Start the Setup**

1. **Run the Redis Container**:
   Use Docker Compose to set up and run the Redis container in detached mode:
   ```bash
   docker compose up --build -d
   ```
   This command ensures the Redis server is running and ready to accept connections.

2. **Run the Spring Boot Application**:
   Start your Spring Boot application with Maven:
   ```bash
   mvn clean spring-boot:run
   ```
   This will launch the backend service, typically on `http://localhost:8080`.

3. **Execute below command**
   ```basg
   docker exec -it <container-name or id> bash
   ```
   
4. **Monitor Redis Commands**:
   Use the `redis-cli` to monitor Redis activity in real-time:
   ```bash
   redis-cli monitor
   ```

5. **Open another linux terminal and enter**
    ```bash
   redis-cli
   ```

---

### **Error Handling**

1. **Redis Container Issues**:
   - Ensure the container is running:
     ```bash
     docker ps
     ```
   - Restart the container if necessary:
     ```bash
     docker compose up --build -d
     ```

2. **Spring Boot Issues**:
   - Check the logs for any errors during startup.
   - Ensure Redis is properly connected and configured in the Spring Boot application.

3. **Postman Errors**:
   - `Could not get any response`: Check if the Spring Boot application is running and accessible on `http://localhost:8080`.
   - `404 Not Found`: Ensure the endpoint path and method are correct.

---

### **Conclusion**

You are now ready to interact with your Redis-backed Spring Boot application using Postman. Monitor Redis operations in real-time using `redis-cli` to verify the actions performed by the API.
