# TaskSketch

Hey! This is a minimalist to-do list web app I built for a university project. Instead of a generic dashboard, I went with a hand-drawn / sketch-style UI to make it feel a bit more personal and organic. 

Under the hood, it's built using classic Java web tech: **Servlets, JSP, and raw JDBC** connecting to a **MySQL** database. Everything follows a straightforward MVC pattern.

## tech stack
- **Backend:** Java Servlets, JDBC
- **Frontend:** JSP, custom vanilla CSS (hand-drawn aesthetic)
- **Database:** MySQL
- **Build / Run:** Maven, Apache Tomcat 9

## features
- basic user registration and login
- create tasks, toggle status (pending/completed), edit task names, and delete them
- clean layout with a sketch UI
- automated setup scripts so you don't have to configure Tomcat manually

---

## how to run it

I've included a few bash scripts to make the setup process as painless as possible on a Mac. It'll automatically download Tomcat and set up the MySQL schemas for you.

### prerequisites
- Java (JDK 17+)
- Homebrew (for installing maven/mysql if you don't have them)

### step 1: environment variables
Create a `.env` file in the root folder for your database credentials. You can just copy the `.env.example` file and tweak it:
```ini
DB_USER=tasksketch
DB_PASSWORD=your_secure_password
```

### step 2: initial setup
Run the setup script. This checks for java, installs maven/mysql via brew if needed, sets up the mysql database (`tasksketch_db`), and downloads a local copy of Tomcat 9 into a hidden `.tomcat` folder so it doesn't mess with your global config.
```bash
./setup.sh
```

### step 3: start the app
Run the start script. This tells maven to compile and package the app into a `.war`, spins up the local Tomcat instance, deploys everything, and opens it up in your web browser.
```bash
./run.sh
```

You can login with the dummy account `demo@example.com` (password: `demo123`), or just make a new one.

### stopping the server
When you're done, just run the stop script to kill the local Tomcat server safely:
```bash
./stop.sh
```

---

## project structure
- `src/main/java...` : contains the Java beans (Model), DAOs (Database Access), and Servlets (Controllers)
- `src/main/webapp/` : all the `.jsp` pages and the custom `style.css`
- `sql/schema.sql` : the raw table creation script just in case you need it manually
- `setup.sh` / `run.sh` / `stop.sh` : the automation scripts
