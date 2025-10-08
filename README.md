# Unified File Format Converter

A simple and extensible Java Swing desktop application for converting files between various formats.

## Features

- **Simple User Interface**: Easy-to-use graphical interface for selecting files and conversion formats.
- **Automatic Format Detection**: The application automatically detects the format of the input file.
- **Multiple Format Support**: Supports a variety of common file formats.

### Supported Conversions

- CSV to JSON
- JSON to CSV
- JSON to XML
- CSV to Excel
- Text to PDF

## Technologies Used

- **Java 21**: Core application language.
- **Java Swing**: For the graphical user interface.
- **Maven**: For project build and dependency management.
- **Jackson**: For JSON and XML processing.
- **Apache POI**: For working with Microsoft Office formats like Excel.
- **iText**: For creating and manipulating PDF files.

## How to Build

To build the project, you need to have Java 21 and Maven installed.

1. Clone the repository:
   ```sh
   git clone <repository-url>
   ```
2. Navigate to the project directory:
   ```sh
   cd unified-file-converter
   ```
3. Run the Maven package command:
   ```sh
   mvn package
   ```
   This will compile the source code, run tests, and create a runnable JAR file with all dependencies in the `target` directory.

## How to Run

Once the project is built, you can run the application from the command line:

```sh
java -jar target/unified-file-converter-1.0.0.jar
```

## How to Use

1.  **Launch the application.**
2.  Click the **"Browse"** button to select the input file you want to convert.
3.  Select the desired output format from the dropdown menu.
4.  Click the **"Convert"** button.
5.  A "Save As" dialog will appear. Choose the location and name for your converted file.
6.  The application will perform the conversion and show the status in the status bar. A log of the conversion will also be available.
