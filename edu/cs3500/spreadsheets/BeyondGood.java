package edu.cs3500.spreadsheets;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

import edu.cs3500.spreadsheets.controller.Controller;
import edu.cs3500.spreadsheets.controller.Features;
import edu.cs3500.spreadsheets.model.Coord;
import edu.cs3500.spreadsheets.model.IViewWorksheet;
import edu.cs3500.spreadsheets.model.IWorksheet;
import edu.cs3500.spreadsheets.model.SimpleWorksheetBuilder;
import edu.cs3500.spreadsheets.model.WorksheetReader;
import edu.cs3500.spreadsheets.view.IView;
import edu.cs3500.spreadsheets.view.ViewText;
import edu.cs3500.spreadsheets.view.WorksheetView;
import edu.cs3500.spreadsheets.view.WorksheetViewEditable;

/**
 * The main class for our program.
 */
public class BeyondGood {
  /**
   * The main entry point.
   *
   * @param args any command-line arguments
   */
  public static void main(String[] args) {
    IView view;
    String inv = "Invalid command.";
    String fileName = "";
    IViewWorksheet blankWSV = new SimpleWorksheetBuilder().createWorksheet();
    IWorksheet blankWSEdit = new SimpleWorksheetBuilder().createWorksheet();

    // if command-line argument is malformed, print statement and exit
    boolean wellFormedSoFar = true;
    boolean commandExecuted = false;
    int currentArg = 0;

    while (currentArg < args.length && wellFormedSoFar && !commandExecuted) {
      switch (args[currentArg]) {
        case "-in":
          if (currentArg + 1 >= args.length || currentArg != 0) {
            wellFormedSoFar = false;
          } else {
            fileName = args[currentArg + 1];
            currentArg++;
          }
          break;
        case "-eval":
          if (fileName.isEmpty() || currentArg + 1 >= args.length || currentArg != 2) {
            wellFormedSoFar = false;
          } else {
            evalCommand(fileName, args[currentArg + 1]);
            commandExecuted = true;
          }
          break;
        case "-save":
          if (fileName.isEmpty() || currentArg + 1 >= args.length || currentArg != 2) {
            wellFormedSoFar = false;
          } else {
            saveCommand(fileName, args[currentArg + 1]);
            commandExecuted = true;
          }
          break;
        case "-gui":
          if (fileName.isEmpty()) {
            view = new WorksheetView(blankWSV);
            view.render();
            commandExecuted = true;
          } else {
            fileGuiCommand(fileName);
            commandExecuted = true;
          }
          break;
        case "-edit":
          if (fileName.isEmpty()) {
            Features control = new Controller(blankWSEdit);
            view = new WorksheetViewEditable(blankWSEdit);
            control.setView(view);
            view.render();
            commandExecuted = true;
          } else {
            fileEditCommand(fileName);
            commandExecuted = true;
          }
          break;
        default:
          wellFormedSoFar = false;
      }
      currentArg++;
    }

    if (!wellFormedSoFar) {
      System.out.println(inv);
    }
  }

  private static void evalCommand(String fileName, String cellName) {
    IViewWorksheet ws;
    try {
      Readable readable = new FileReader(fileName);
      ws = WorksheetReader.read(new SimpleWorksheetBuilder(), readable);
      // cell name is not empty, caught in main switch
      if (Coord.isCellRef(cellName)) {
        Coord cellCoord = Coord.symbolToCoord(cellName);
        System.out.println(ws.evaluateCell(cellCoord.col, cellCoord.row));
      } else {
        System.out.println("Invalid cell name.");
      }
    } catch (IOException e) {
      System.out.println("File not found.");
    } catch (IllegalArgumentException e1) {
      System.out.println("Error in cell " + cellName + ": " + e1.getMessage());
    }
  }

  private static void saveCommand(String fileName, String newFileName) {
    IViewWorksheet ws;
    try {
      Readable readable = new FileReader(fileName);
      ws = WorksheetReader.read(new SimpleWorksheetBuilder(), readable);
      File newFile = new File(newFileName);
      PrintWriter pw = new PrintWriter(newFile);
      IView vt = new ViewText(ws, pw);
      vt.render();
      pw.close();
    } catch (IOException e) {
      System.out.println("File not found.");
    }
  }

  private static void fileGuiCommand(String fileName) {
    IViewWorksheet ws;
    try {
      Readable readable = new FileReader(fileName);
      ws = WorksheetReader.read(new SimpleWorksheetBuilder(), readable);
      IView visualView = new WorksheetView(ws);
      visualView.render();
    } catch (IOException e) {
      System.out.println("File not found.");
    }
  }

  private static void fileEditCommand(String fileName) {
    IWorksheet ws;
    try {
      Readable readable = new FileReader(fileName);
      ws = WorksheetReader.read(new SimpleWorksheetBuilder(), readable);
      Features control = new Controller(ws);
      IView editView = new WorksheetViewEditable(ws);
      control.setView(editView);
      editView.render();
    } catch (IOException e) {
      System.out.println("File not found.");
    }
  }
}