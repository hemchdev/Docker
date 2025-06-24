# Notes: Docker (Virtualization & Containerization)

## Table of Contents

1. [The Core Problem: "It Works on My Machine"](#1-the-core-problem-it-works-on-my-machine "null")
    
2. [Introduction to Virtualization](#2-introduction-to-virtualization "null")
    
3. [Problems with Virtualization](#3-problems-with-virtualization "null")
    
4. [Containerization: The Modern Solution](#4-containerization-the-modern-solution "null")
    
5. [What is Docker?](#5-what-is-docker "null")
    
6. [Setting Up Docker](#6-setting-up-docker "null")
    
7. [Exploring Docker and First Commands](#7-exploring-docker-and-first-commands "null")
    
8. [Managing Images and Containers](#8-managing-images-and-containers "null")
    
9. [Docker Architecture](#9-docker-architecture "null")
    
10. [Running a Java Application in a Container](#10-running-a-java-application-in-a-container "null")
    
11. [Preparing a Spring Boot Application for Docker](#11-preparing-a-spring-boot-application-for-docker "null")
    
12. [Automating Image Creation with a Dockerfile](#12-automating-image-creation-with-a-dockerfile "null")
    
13. [Building a Database-Driven Spring Boot App](#13-building-a-database-driven-spring-boot-app "null")
    
14. [Defining Services with Docker Compose](#14-defining-services-with-docker-compose "null")
    
15. [Connecting Services with Docker Networking](#15-connecting-services-with-docker-networking "null")
    
16. [Persisting Data with Docker Volumes](#16-persisting-data-with-docker-volumes "null")
    

## 1. The Core Problem: "It Works on My Machine"

The fundamental challenge in software development is ensuring that an application works reliably across different environments.

- **The Development Phase:** A developer writes code, connects to a database (like MySQL, Postgres), sets up a web server, and configures numerous settings. On their local machine, the application works perfectly.
    
- **The Handoff Challenge:** The problem begins when the project is shared.
    
    - **To Colleagues:** Team members must replicate the entire setup perfectly. This involves installing the correct versions of all software and matching every configuration detail.
        
    - **To the Testing Team:** The testing team faces the same challenge. They need to build the exact environment to validate the application. Any mismatch can lead to failures, which are sent back to the developer.
        
    - **To the Operations (Ops) Team:** For production deployment, the ops team must recreate the setup on powerful servers, which often run different operating systems (e.g., Linux) and have different hardware than the developer's laptop.
        
- **Why It Fails:** An application can fail in a new environment for many reasons:
    
    - **Mismatched Dependencies:** Different versions of libraries, databases, or web servers.
        
    - **OS Incompatibility:** Features that work on the developer's OS (e.g., Windows) might not work on the server's OS (e.g., Linux).
        
    - **Configuration Errors:** The manual process of setting up the environment is complex and prone to errors.
        

This leads to the classic developer response: "But it works on my machine!" The core issue is that we are only shipping the application code, not the environment it needs to run. The ideal solution would be to package the application _with_ its entire configured environment, but simply copying a developer's hard drive is not a feasible or effective solution.

## 2. Introduction to Virtualization

Virtualization is a technology that offers a solution to this problem by allowing you to run multiple operating systems on a single physical machine.

### How a Standard Computer Works

1. **Hardware:** The physical components (CPU, RAM, Motherboard, etc.).
    
2. **Operating System (OS):** Software that manages the hardware and provides services for applications (e.g., Windows, macOS, Linux).
    
3. **Applications (Apps):** Software that users interact with.
    

The flow is: **User -> App -> OS -> Hardware**.

### How Virtualization Works

1. **Hardware:** Your physical machine.
    
2. **Host OS:** The main operating system on your hardware (e.g., Windows).
    
3. **Hypervisor:** A special software (like VMware, VirtualBox) that creates and manages virtual machines.
    
4. **Virtual Hardware:** The simulated hardware environment created by the hypervisor.
    
5. **Guest OS:** A second, complete OS installed on the virtual hardware (e.g., Ubuntu Linux).
    
6. **Your Application:** Runs inside the **Guest OS**.
    

### Solving the Problem with Virtualization

Instead of just sharing application code, you share the _entire Guest OS_ as a single file called an **image**. This ensures the application runs in the exact same environment on any machine with a hypervisor.

### Other Use Cases for Virtualization

- **Server Isolation:** Run multiple, isolated applications on one physical server, each within its own Guest OS (Virtual Machine).
    
- **Resource Utilization:** Efficiently partition a single powerful server into many smaller virtual servers.
    

## 3. Problems with Virtualization

While powerful, virtualization has significant drawbacks:

- **High Resource Consumption:** Running a full, second OS consumes a large amount of CPU and RAM. Running multiple applications means running multiple Guest OSes, which is very heavy.
    
- **Large Size:** OS images are very large (often gigabytes).
    
- **Slow Startup:** Booting up an entire Guest OS takes time.
    
- **Licensing Costs:** You may need to pay for licenses for both the Host OS and the Guest OS.
    

## 4. Containerization: The Modern Solution

Containerization solves the problems of virtualization by being far more lightweight and efficient. It packages an application with all its dependencies into a single, portable unit called a **container**.

### The Shipping Container Analogy

Think of a shipping container. Goods are packed into a standard-sized container at the factory. This same container is then moved via truck, ship, and another truck to the final destination without ever being unpacked and repacked. The container standardizes transport.

Software containers do the same thing: the application is "packed" into a container on the developer's machine, and that _exact same container_ is moved to testing, production, and the cloud.

### How Containerization Works

Containerization eliminates the need for a Guest OS. Instead, it uses a **Container Engine** (like Docker).

1. **Hardware:** Your physical machine.
    
2. **Host OS:** The main operating system (e.g., Windows, Linux).
    
3. **Container Engine (e.g., Docker):** Software that creates and runs containers.
    
4. **Containers:** Each container includes your **Application** and its **Dependencies** (libraries, compilers, etc.). Critically, all containers share the Host OS's kernel.
    

### Key Advantages of Containerization

- **Lightweight & Fast:** Containers don't include a Guest OS, so they are much smaller (megabytes instead of gigabytes) and start almost instantly.
    
- **Efficient:** Since there is only one OS, you can run many more containers on a single machine compared to virtual machines, saving CPU and RAM.
    
- **Isolation:** Containers are isolated from each other, providing security without the overhead of a full OS.
    
- **Portability:** A container created on a developer's machine will run identically on any other machine (testing, production, cloud) that has a container engine installed.
    
- **Consistency:** It completely solves the "it works on my machine" problem.
    
- **Scalability:** You can easily create multiple instances of the same container to handle increased load.
    

## 5. What is Docker?

**Docker** is a platform and a set of tools designed to make it easy to create, deploy, and run applications using containers. It provides everything needed to achieve containerization.

### Key Docker Components

- **Docker Engine:** The core background service that creates, manages, and runs containers. Users interact with the engine through a command-line interface (CLI).
    
- **Image:** A lightweight, standalone, executable package that includes everything needed to run a piece of software, including the code, a runtime, libraries, environment variables, and config files. It's a read-only template or blueprint.
    
- **Container:** A runnable instance of an image. You can create, start, stop, move, or delete a container. It's the "running" version of the blueprint.
    
- **Dockerfile:** A text file that contains a list of commands and instructions that Docker uses to build a custom image automatically. You specify the base OS, dependencies, application code, and configurations here.
    
- **Docker Hub:** A cloud-based registry service (like GitHub or an App Store) where you can find and share container images. Many official images for popular software (like MongoDB, Tomcat, Ubuntu) are available here.
    
- **Networking:** Docker provides its own networking components to allow containers to communicate with each other in an isolated environment, and also allows you to "expose" ports to connect applications on your host machine to applications inside a container.
    
- **Volumes:** The preferred mechanism for persisting data generated by and used by Docker containers. They allow data to exist even after a container is removed.
    
- **Docker Compose:** A tool for defining and running multi-container Docker applications. With a single command, you can start and connect all the services (e.g., a web server, a database, a caching service) required for your application.
    

## 6. Setting Up Docker

Getting started with Docker involves installing Docker Desktop, a graphical application that includes the Docker Engine, CLI, and other tools.

### Verifying the Installation

Before starting, you can check if Docker is already installed by opening a terminal (Command Prompt, PowerShell, or Terminal) and running:

```
docker version
```

If it's not installed, you will see an error like "`docker` is not recognized..."

### Downloading and Installing Docker Desktop

1. **Go to the official Docker website:** Search for "Docker download" or go directly to docker.com.
    
2. **Choose your OS:** The website will provide download links for **Docker Desktop** for Windows, Mac (Intel and Apple Silicon chips), and Linux.
    
3. **Installation:**
    
    - **Windows/Mac:** Download the installer and follow the straightforward on-screen instructions.
        
    - **Linux:** The website will provide a series of commands to run in your terminal to install Docker.
        
4. **System Requirements:** Docker can be resource-intensive. For a smooth experience on Windows, 16GB of RAM is recommended, though it can run on 8GB.
    

### Alternative: Play with Docker (Online Labs)

If you don't want to or cannot install Docker on your local machine, you can use **Play with Docker**.

- It's a free, browser-based environment that gives you access to a Docker instance.
    
- You need a Docker Hub account to log in.
    
- **Limitation:** Sessions are temporary and typically last for 4 hours, making it great for quick tests but not for long-term projects.
    

### Post-Installation Steps

1. **Restart your Terminal:** After installation is complete, close and reopen your command line/terminal.
    
2. **Verify Again:** Run `docker version` again. This time, it should successfully display the version numbers for the Docker client and engine.
    
3. **Launch Docker Desktop:** Open the Docker Desktop application. It will show a dashboard where you can manage your containers, images, and volumes.
    
4. **Pulling your first image:** You can use the Docker Desktop search bar or the command line to download (or "pull") an image from Docker Hub. For example, to get the lightweight Ubuntu image, you would run:
    
    ```
    docker pull ubuntu
    ```
    
    This image will now appear in the "Images" tab of Docker Desktop, ready to be run as a container.
    

## 7. Exploring Docker and First Commands

Once Docker is installed, you can interact with it through the Docker Desktop application or, more commonly, through the command line.

### Images vs. Containers: A Reminder

- **Image:** A lightweight, read-only template (blueprint) containing instructions to create a container.
    
- **Container:** A running instance of an image. It's the actual, working software. Containers are heavier than images as they are live processes.
    

### Finding Images on Docker Hub

Docker Hub is the default public registry for Docker images. You can search for images directly on the hub.docker.com website or through the Docker Desktop UI.

- **Docker Official Image:** The most trusted images, maintained by Docker.
    
- **Verified Publisher:** Images from trusted third-party vendors.
    
- **Community Images:** Images created and shared by the general public. It's best to use official or verified images when possible.
    

### Running Your First Container

The easiest way to start is with the `hello-world` image. This is typically done from the command line (Terminal on Mac/Linux, CMD/PowerShell on Windows).

```
docker run hello-world
```

When you execute this command, Docker performs the following steps:

1. **Checks Locally:** It looks for the `hello-world:latest` image on your local machine.
    
2. **Pulls from Docker Hub:** If it can't find the image locally, it automatically downloads (pulls) it from Docker Hub.
    
3. **Creates and Runs a Container:** It creates a new container from the image, runs it, and displays the "Hello from Docker!" message.
    
4. **Exits:** The `hello-world` container's job is just to print a message, so it stops and exits immediately after.
    

### Essential Commands to Check Status

- **List all local images:**
    
    ```
    docker images
    ```
    
    This shows all the images you have downloaded, including their repository name, tag (version), image ID, and size.
    
- **List running containers:**
    
    ```
    docker ps
    ```
    
    This command shows only the containers that are currently active and running.
    
- **List all containers (running and stopped):**
    
    ```
    docker ps -a
    ```
    
    This is useful for seeing containers that have completed their task and exited, like `hello-world`. The output includes the container ID, the image it was created from, its status, and a randomly assigned name (e.g., `nice_borg`).
    

## 8. Managing Images and Containers

This section covers the essential commands for managing the full lifecycle of your Docker images and containers.

### Getting Help

To see a list of all available Docker commands, you can always use the `help` command.

```
docker help
```

### Removing Containers and Images

To keep your system clean, you'll often need to remove old containers and images.

- **Remove a container:**
    
    ```
    docker rm <container_id_or_name>
    ```
    
    You can use the full ID or just the first few unique characters.
    
- **Remove an image:**
    
    ```
    docker rmi <image_id>
    ```
    
- **The Golden Rule:** You cannot remove an image if there is a container (even a stopped one) that was created from it. You must remove the dependent container(s) first. If you try, Docker will show an error.
    

### The Manual, Step-by-Step Workflow

While `docker run` is a convenient shortcut, it's helpful to understand the individual steps it combines.

1. **Search for an image** on Docker Hub:
    
    ```
    docker search <image_name>
    # Example: docker search hello-world
    ```
    
2. **Pull (download) the image** from Docker Hub to your local machine:
    
    ```
    docker pull <image_name>
    # Example: docker pull hello-world
    ```
    
    After this step, `docker images` will show the new image, but no container has been created yet.
    
3. **Create a container** from the image. This prepares the container but does not start it:
    
    ```
    docker create <image_name>
    # Example: docker create hello-world
    ```
    
    This command will output a long container ID. Now, `docker ps -a` will show the newly created (but stopped) container.
    
4. **Start the container** to run it:
    
    ```
    docker start <container_id_or_name>
    # Example: docker start b1d... (using the first few chars of the ID)
    ```
    

### Other Lifecycle Commands

- **Stop a running container:**
    
    ```
    docker stop <container_id_or_name>
    ```
    
- **Pause a running container:** This suspends all processes within the container without stopping it.
    
    ```
    docker pause <container_id_or_name>
    ```
    
    You cannot pause a container that is already stopped.
    

Remember, the `docker run` command performs the `pull` (if necessary), `create`, and `start` steps all in one go.

## 9. Docker Architecture

To understand how Docker works, it's essential to know its client-server architecture.

- **Docker Client:** This is the primary way you interact with Docker. When you type commands like `docker run` or `docker pull` into your terminal, you are using the Docker client.
    
- **Docker Daemon (dockerd):** This is the Docker engine, a persistent background process that listens for API requests from the Docker client. The daemon does all the heavy lifting: building, running, and managing your containers. It also manages Docker objects like images, networks, and volumes.
    
- **Registry:** This is a remote repository where Docker images are stored. **Docker Hub** is the default public registry, but companies can also host their own private registries.
    

### The Workflow

1. A user issues a command to the **Docker Client** (e.g., `docker pull ubuntu`).
    
2. The Docker Client sends this command to the **Docker Daemon** via a REST API.
    
3. The **Docker Daemon** receives and processes the request.
    
    - If the command is to `pull` an image, the daemon connects to the **Registry** (e.g., Docker Hub), finds the image, and downloads it to the local machine.
        
    - If the command is to `run` a container, the daemon uses a local image to create and start the container, managing its network and storage volumes.
        
4. The daemon is responsible for the entire lifecycle of Docker objects.
    

## 10. Running a Java Application in a Container

To run a Java application, you need a Java Development Kit (JDK). Instead of installing it on your host machine, you can run it inside a container, bundling your application with its runtime.

### Finding a JDK Image

1. **Search Docker Hub:** You can look for official Java images. A popular and well-maintained option is `openjdk`.
    
    ```
    docker search openjdk
    ```
    
2. **Check Architecture:** Images are built for specific computer architectures (e.g., `amd64` for most PCs, `arm64` for Apple Silicon). Docker Hub lists the supported architectures for each image tag. It's crucial to pick one that matches your machine.
    

### Pulling the Image

Once you've found a suitable image and tag, pull it to your local machine.

```
docker pull openjdk:22-jdk
```

This command downloads the OpenJDK version 22 image.

### Running JShell Interactively

To use the tools inside the JDK image, like `jshell` (an interactive Java REPL), you need to run the container in **interactive mode**.

```
docker run -it openjdk:22-jdk
```

- `-i` (interactive): Keeps STDIN open even if not attached.
    
- `-t` (tty): Allocates a pseudo-TTY, which connects your terminal to the container's input/output.
    

This command will start the container and drop you directly into a `jshell` prompt. You can now execute Java code inside the container:

```
jshell> int num = 9;
num ==> 9

jshell> System.out.println("Hello from a container!");
Hello from a container!
```

This demonstrates that you have a fully functional JDK environment running in an isolated container, ready for you to add your own application code.

## 11. Preparing a Spring Boot Application for Docker

The goal is to run our own custom application inside a container. Here, we'll create a simple Spring Boot web application and package it into an executable JAR file, making it ready for containerization.

### Step 1: Create the Spring Boot Project

The easiest way to start is with the **Spring Initializr** (`start.spring.io`).

- **Project:** Maven
    
- **Language:** Java
    
- **Spring Boot Version:** 3.1.5 (or any stable version)
    
- **Project Metadata:**
    
    - Group: `com.telescope`
        
    - Artifact: `rest-demo`
        
- **Packaging:** Jar
    
- **Dependencies:** Add `Spring Web` to build a web application.
    
- Click "Generate" to download the project zip file.
    

### Step 2: Write a Simple Web Controller

After unzipping and opening the project in your IDE (e.g., IntelliJ), create a simple controller to handle web requests.

1. Create a new Java class named `HelloController`.
    
2. Add the following code:
    
    ```
    import org.springframework.web.bind.annotation.RequestMapping;
    import org.springframework.web.bind.annotation.RestController;
    
    @RestController
    public class HelloController {
    
        @RequestMapping("/")
        public String greet() {
            return "Hello World!";
        }
    }
    ```
    
    - `@RestController`: Marks this class as a controller where every method returns a domain object instead of a view.
        
    - `@RequestMapping("/")`: Maps HTTP requests to the root URL (`/`) to the `greet()` method.
        

### Step 3: Run and Test Locally

Before packaging, make sure the application works.

1. Run the main application class from your IDE.
    
2. **Port Conflict:** If the default port `8080` is in use, you'll see an error. You can change it by adding the following line to `src/main/resources/application.properties`:
    
    ```
    server.port=8081
    ```
    
3. Restart the application. Open a web browser and navigate to `http://localhost:8081`. You should see "Hello World!".
    

### Step 4: Package the Application into a JAR

To run the application standalone, you need to package it into an executable JAR file.

1. Since this is a Maven project, you can use the Maven wrapper to create the package.
    
2. Open a terminal in the root directory of your project and run:
    
    ```
    mvn package
    ```
    
3. This command compiles your code, runs tests, and packages everything into a single JAR file located in the `target/` directory (e.g., `target/rest-demo-0.0.1-SNAPSHOT.jar`).
    

### Step 5: Run the Executable JAR

You can now run this JAR file from your terminal without needing an IDE.

1. Make sure you've stopped the application running from your IDE.
    
2. In your terminal, run the following command (adjust the JAR file name if needed):
    
    ```
    java -jar target/rest-demo-0.0.1-SNAPSHOT.jar
    ```
    
3. The application will start up again. Verify it's working by visiting `http://localhost:8081` in your browser.
    

Now that we have a self-contained, executable JAR, the next step is to get this file into our JDK container and run it there.

## 12. Automating Image Creation with a Dockerfile

Running multiple commands manually to copy files and commit changes to create an image is tedious and not scalable. The standard, automated way to create a custom Docker image is by using a `Dockerfile`.

A `Dockerfile` is a text document that contains all the commands, in order, needed to build a given image.

### Step 1: Create the Dockerfile

In the root directory of your Spring Boot project, create a new file named `Dockerfile` (no file extension).

### Step 2: Write the Dockerfile Instructions

Add the following content to your `Dockerfile`. Each instruction creates a new layer in the image.

```
# Start with a base image that has Java installed
FROM openjdk:22-jdk

# Add the compiled JAR file from our host machine into the image
# Format: ADD <source_on_host> <destination_in_image>
ADD target/rest-demo-0.0.1-SNAPSHOT.jar rest-demo.jar

# Tell Docker what command to run when a container from this image starts
ENTRYPOINT ["java", "-jar", "rest-demo.jar"]
```

- **`FROM`**: Specifies the base image to build upon. We're using the `openjdk` image because our application needs a Java runtime.
    
- **`ADD`**: Copies files from a source on the host machine to a destination inside the image. Here, we copy our application's JAR file into the root directory of the image and rename it for simplicity.
    
- **`ENTRYPOINT`**: Configures the container to run as an executable. This is the command that will be executed when the container starts.
    

### Step 3: Build the Image from the Dockerfile

Now, use the `docker build` command to create your custom image. Run this command from the root directory of your project.

```
docker build -t rest-demo:v3 .
```

- `docker build`: The command to build an image from a Dockerfile.
    
- `-t rest-demo:v3`: The `-t` flag allows you to **tag** the image with a name and optional tag (version). Here, the name is `rest-demo` and the tag is `v3`.
    
- `.`: The final dot specifies the **build context**, which is the set of files at the specified path. `.` means the current directory. Docker needs this context to find the `Dockerfile` and any files you `ADD` or `COPY`.
    

Docker will execute the steps in the `Dockerfile`, using the cache for steps that haven't changed, and output a new image. You can verify this by running `docker images`.

### Step 4: Run the New Custom Image

Finally, run a container from your newly created image.

```
docker run -p 8081:8081 rest-demo:v3
```

- `-p 8081:8081`: This maps the port. Our Spring Boot app runs on port `8081` inside the container. This command exposes that internal port to port `8081` on our host machine.
    
- `rest-demo:v3`: The name and tag of the image we want to run.
    

The container will start, and the `ENTRYPOINT` command will execute, running your Spring Boot application. You can now go to `http://localhost:8081` in your browser and see "Hello World!" being served from your containerized application.

## 13. Building a Database-Driven Spring Boot App

Real-world applications often require multiple services working together, such as a web application and a database. This section covers building a Spring Boot application that connects to a PostgreSQL database, setting the stage for managing it with Docker Compose.

### Step 1: Create a Spring Boot Project with Database Dependencies

Use the **Spring Initializr** (`start.spring.io`) with the following settings:

- **Project:** Maven
    
- **Language:** Java
    
- **Artifact:** `student-app`
    
- **Packaging:** Jar
    
- **Dependencies:**
    
    - `Spring Web` (for the REST controller)
        
    - `Spring Data JPA` (to easily interact with the database)
        
    - `PostgreSQL Driver` (the specific driver for our database)
        

### Step 2: Create the `Student` Entity and Repository

- **Entity:** Define a `Student` class annotated with `@Entity`, `@Id`, and `@GeneratedValue`. This class represents the data in your database table. It should have fields like `id`, `name`, and `age`, along with constructors, getters, and setters.
    
- **Repository:** Create a `StudentRepo` interface that extends `JpaRepository<Student, Integer>`. Annotate it with `@Repository`. This gives you standard database methods for free.
    

### Step 3: Create the Controller

Create a `StudentController` that uses the repository to fetch data.

```
@RestController
public class StudentController {

    @Autowired
    private StudentRepo repo;

    @GetMapping("getStudents")
    public List<Student> getStudents() {
        return repo.findAll();
    }
}
```

### Step 4: Configure `application.properties`

In `src/main/resources/application.properties`, configure the database connection for local testing.

```
# Server Port
server.port=8090

# Datasource Properties for local testing
spring.datasource.url=jdbc:postgresql://localhost:5432/studentdb
spring.datasource.username=postgres
spring.datasource.password=your_password

# JPA Properties
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.sql.init.mode=always
```

- `ddl-auto=update`: Hibernate will update the database schema based on your entities.
    
- `init.mode=always`: Ensures `data.sql` is always run on startup.
    

### Step 5: Add Initial Data and Test Locally

- Create a `data.sql` file in `src/main/resources` with some `INSERT` statements to populate the database on startup.
    
- Run the application. If your local PostgreSQL server is running correctly, you should be able to navigate to `http://localhost:8090/getStudents` and see the data.
    

With a working application that depends on a separate database, we're now ready to define and run both services together using Docker Compose.

## 14. Defining Services with Docker Compose

When your application consists of multiple services (like an app and a database), you need a way to define, run, and link them together. This is the job of **Docker Compose**. It uses a YAML file to configure all of your application's services.

### Step 1: Package the Application and Create its Dockerfile

1. **Package:** Make sure your `student-app` is packaged into a JAR file. You can configure the `pom.xml` to produce a simpler final name (e.g., `student-app.jar`) and run `mvn clean package`.
    
2. **Dockerfile:** Create a `Dockerfile` in the root of the `student-app` project. This file is responsible only for building the application image.
    
    ```
    FROM openjdk:22-jdk
    ADD target/student-app.jar student-app.jar
    ENTRYPOINT ["java","-jar","student-app.jar"]
    ```
    

### Step 2: Create the `docker-compose.yml` File

In the root of your project, create a file named `docker-compose.yml`. This file will define our two services: `app` and `postgres`.

```
version: '3.8' # Specifies the Compose file format version

services: # Defines all the services (containers) that make up your app
  
  # The definition for our Spring Boot application container
  app:
    build: . # Build an image from the Dockerfile in the current directory
    ports:
      - "8090:8080" # Map port 8090 on the host to port 8080 in the container

  # The definition for our PostgreSQL database container
  postgres:
    image: postgres:latest # Use the official postgres image from Docker Hub
    ports:
      - "5433:5432" # Map port 5433 on the host to port 5432 in the container
    environment:
      - POSTGRES_USER=hemanth
      - POSTGRES_PASSWORD=1234
      - POSTGRES_DB=employeedb
```

### Step 3: Run with Docker Compose

Instead of using `docker run`, you now use a single `docker-compose` command from your terminal in the same directory as the `docker-compose.yml` file.

```
docker-compose up --build
```

- `docker-compose up`: This command starts (and creates, if necessary) all the services defined in the `docker-compose.yml` file.
    
- `--build`: This flag tells Compose to build the `app` image from its `Dockerfile` before starting the service.
    

### Step 4: Identifying the Problem

When you run this command, both containers will start. However, the `app` container will quickly fail and exit. Checking the logs (`docker-compose logs app`), you will see a `Connection refused` error.

**Why?** By default, Docker Compose creates a network for your application and attaches each service to it. However, the application and database containers are still isolated. Your application, configured to connect to `localhost:5432`, is looking for a database on its _own_ `localhost`, not on the `postgres` container's `localhost`.

To fix this, we need to make the application aware of the database service on the Docker network and ensure the services start in the correct order.

## 15. Connecting Services with Docker Networking

To fix the `Connection refused` error, we need to enable communication between the `app` container and the `postgres` container. This involves two key steps: updating the application's connection string and defining a shared network in Docker Compose.

### Step 1: Update Application Properties for Container Networking

Inside a Docker network, containers can refer to each other by their service name. The `localhost` in the `app` container refers to itself, not the `postgres` container.

Modify `src/main/resources/application.properties` to use the service name (`postgres`) as the hostname.

```
# Old connection string
# spring.datasource.url=jdbc:postgresql://localhost:5432/studentdb

# New connection string for Docker networking
spring.datasource.url=jdbc:postgresql://postgres:5432/studentdb

# Also, update credentials to match the docker-compose environment variables
spring.datasource.username=naveen
spring.datasource.password=1234
```

Docker Compose provides internal DNS resolution, so the `app` container can find the `postgres` container using its service name.

### Step 2: Define a Shared Network in Docker Compose

While Docker Compose creates a default network, explicitly defining one gives you more control and makes the configuration clearer.

Update your `docker-compose.yml` to create a network and attach both services to it.

```
version: '3.8'

services:
  app:
    build: .
    ports:
      - "8090:8090" # Host port : Container port
    networks: # Attach this service to the 's-network'
      - s-network

  postgres:
    image: postgres:latest
    ports:
      - "5433:5432"
    environment:
      - POSTGRES_USER=naveen
      - POSTGRES_PASSWORD=1234
      - POSTGRES_DB=studentdb
    networks: # Attach this service to the same 's-network'
      - s-network

# Top-level key to define networks
networks:
  s-network: # Name of our custom network
    driver: bridge # The default driver for single-host networking
```

### Step 3: Relaunch and Verify

1. Stop the currently running containers:
    
    ```
    docker-compose down
    ```
    
2. Rebuild and start the services with the new configuration:
    
    ```
    docker-compose up --build
    ```
    

With these changes, the `app` container can now successfully connect to the `postgres` container over the shared `s-network`. You can visit `http://localhost:8090/getStudents` and see the data being served from the containerized database.

## 16. Persisting Data with Docker Volumes

Our application now works, but the data inside the PostgreSQL container is **ephemeral**. By default, when a container is removed, its entire filesystem is also deleted. This means if we run `docker-compose down`, all the data our application has saved to the database will be lost forever.

To solve this, we need to store the data outside the container in a persistent location. The recommended way to do this in Docker is with **Volumes**. A volume is a storage mechanism managed by Docker that exists independently of a container's lifecycle.

### How Volumes Work

We can create a named volume and "mount" it to a specific directory inside our `postgres` container. The official PostgreSQL image stores its data in the `/var/lib/postgresql/data` directory. By mounting a volume to this path, we tell Docker to store all the database files in our named volume on the host machine, instead of inside the container's temporary filesystem.

### Step 1: Define the Volume in Docker Compose

First, we need to declare a named volume at the top level of our `docker-compose.yml` file. Let's call it `db-data`.

### Step 2: Mount the Volume to the PostgreSQL Service

Next, we attach this named volume to our `postgres` service at the correct data directory.

Update your `docker-compose.yml` file with the `volumes` keys:

```
version: '3.8'

services:
  app:
    build: .
    ports:
      - "8090:8090"
    networks:
      - s-network
    # Make sure app starts after the database is ready
    depends_on:
      - postgres

  postgres:
    image: postgres:latest
    ports:
      - "5433:5432"
    environment:
      - POSTGRES_USER=naveen
      - POSTGRES_PASSWORD=1234
      - POSTGRES_DB=studentdb
    networks:
      - s-network
    # Mount the named volume to the container's data directory
    volumes:
      - db-data:/var/lib/postgresql/data

networks:
  s-network:
    driver: bridge

# Top-level key to define a named volume
volumes:
  db-data:
```

- **`volumes: db-data` (top-level):** This creates a named volume called `db-data`. Docker will manage this volume's storage location on the host machine.
    
- **`volumes: - db-data:/var/lib/postgresql/data` (under `postgres` service):** This mounts the `db-data` volume to the `/var/lib/postgresql/data` directory inside the container.
    
- **`depends_on`**: This new key ensures that Docker Compose will start the `postgres` container _before_ starting the `app` container, preventing connection errors during startup.
    

### Step 3: Verify Data Persistence

1. Run `docker-compose up --build` to start your application.
    
2. Use your application to add, update, or delete some data.
    
3. Bring the services down using `docker-compose down`. This will stop and remove the containers.
    
4. Bring the services back up with `docker-compose up`.
    

This time, when the new `postgres` container starts, Docker will re-attach the existing `db-data` volume. Your database will be in the exact same state as before you brought it down, with all your changes preserved. Your data is now persistent.
