# Use the official Node.js image as a base image
FROM node:14

# Set the working directory inside the container
WORKDIR /usr/src/app

# Copy package.json and package-lock.json to the working directory
COPY package*.json ./

# Install the dependencies
RUN npm install

# Install Express globally
RUN npm install -g express

# Copy the application files to the working directory
COPY . .

# Expose port 8080
EXPOSE 8080

# Define the command to run your application
CMD ["node", "index.js"]
# Use the official Node.js image as a base image
FROM node:14

# Set the working directory inside the container
WORKDIR /usr/src/app

# Copy package.json and package-lock.json to the working directory
COPY package*.json ./

# Install the dependencies
RUN npm install

# Install Express globally
RUN npm install -g express

# Copy the application files to the working directory
COPY . .

# Expose port 8080
EXPOSE 8080

# Define the command to run your application
CMD ["node", "index.js"]


