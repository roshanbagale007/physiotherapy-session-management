# Task 2: Jakarta Servlets 6 & JSP Web Application

**Module Directory:** `web-portal/`  
**Course:** 24MCAC302 - Advance Java Programming (Unit 2)  
**Server Target:** Apache Tomcat 10.1 (Port 8025)  
**Student:** ROSHAN NAMDEO BAGALE (Roll No: 50, Div D)  

---

## 1. Syllabus Unit 2 Mapping

| Syllabus Unit 2 Topic | Implementation in `web-portal` |
| :--- | :--- |
| **2.1 - 2.3: Servlets, Life Cycle & Requests** | `LoginServlet`, `LogoutServlet`, and Dashboard Servlets (`AdminDashboardServlet`, `DoctorDashboardServlet`, etc.) handling HTTP GET and POST requests, headers, and request forwarding. |
| **2.4: Session Management** | `HttpSession` management with 30-minute inactivity timeout configured in `web.xml`, storing principal credentials and roles, and explicit invalidation via `LogoutServlet`. |
| **2.5: Servlet Filters (RBAC Barrier)** | `AuthFilter.java` intercepting all incoming HTTP requests to validate sessions and enforce strict Role-Based Access Control (`ADMIN`, `THERAPIST`, `RECEPTIONIST`, `PATIENT`), triggering HTTP 403 Forbidden on violations. |
| **2.2.1 - 2.2.3: JSP Architecture, EL & JSTL** | Modern JSP views in `WEB-INF/views/` rendered using Jakarta Standard Tag Library (`jakarta.tags.core`) `<c:if>`, `<c:forEach>`, and Expression Language (`${sessionScope.user}`). |

---

## 2. Test User Accounts

| Role | Username | Password | Dedicated Route |
| :--- | :--- | :--- | :--- |
| **ADMIN** | `admin` | `Admin@123` | `http://localhost:8025/web-portal/admin/dashboard` |
| **THERAPIST (Doctor)** | `dr.sameer` | `Doctor@123` | `http://localhost:8025/web-portal/doctor/dashboard` |
| **RECEPTIONIST** | `receptionist` | `Staff@123` | `http://localhost:8025/web-portal/desk/dashboard` |
| **PATIENT** | `rahul.v` | `Patient@123` | `http://localhost:8025/web-portal/patient/dashboard` |

---

## 3. How to Deploy & Run on Apache Tomcat 10.1 in Eclipse

1. Open **Eclipse IDE**.
2. Look at the bottom pane $\rightarrow$ Click on the **Servers** tab.
   *(You will see **Tomcat v10.1 Server at localhost**)*
3. Right-click on **Tomcat v10.1 Server at localhost** $\rightarrow$ Click **Add and Remove...**
4. Select **`web-portal`** from the Available column $\rightarrow$ Click **Add >** $\rightarrow$ Click **Finish**.
5. Right-click the Tomcat server $\rightarrow$ Click **Start** (or click the green Run icon).
6. Open your web browser (Chrome / Edge) and navigate to:
   $$\mathbf{\texttt{http://localhost:8025/web-portal/login}}$$
7. Test logging in with any of the accounts above!

---

## 4. How to Test the Security Filter (AuthFilter)

1. Log in as a Patient using `rahul.v` / `Patient@123`.
2. Notice you land on the **Patient Portal** showing Rahul's diagnosis, progress bar, and appointments.
3. Now, in your browser URL address bar, manually try to visit the Administrator URL:
   `http://localhost:8025/web-portal/admin/dashboard`
4. **Expected Result:** `AuthFilter` intercepts the request and instantly renders the **403 Access Denied** security page!
5. Click **Logout** to test `session.invalidate()` and return safely to the login screen.
