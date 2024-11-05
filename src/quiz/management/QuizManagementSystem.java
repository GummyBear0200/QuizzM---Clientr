package quiz.management;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class QuizManagementSystem {
    private static final Scanner scanner = new Scanner(System.in);
    private Connection connection;
    private QuizDAO quizDAO;
    private userDAO userDAO;
    config Config = new config ();
    public QuizManagementSystem() {
        connection = Config.connectDB();
        if (connection == null) {
            System.out.println("Failed to connect to the database.");
            System.exit(1);
        }
        quizDAO = new QuizDAO(connection);
        userDAO = new userDAO(connection); 
    }

  public void menu() {
    while (true) {
        System.out.println(repeatString("-", 50));
        System.out.println("|          Quiz Management System Menu        |");
        System.out.println(repeatString("-", 50));
        System.out.printf("| %-3s | %-40s |\n", "1", "Quizzes");
        System.out.printf("| %-3s | %-40s |\n", "2", "Questions");
        System.out.printf("| %-3s | %-40s |\n", "3", "Users");
        System.out.printf("| %-3s | %-40s |\n", "4", "Reports");
        System.out.println("-------------------------------------------------|");
        System.out.printf("| %-3s | %-40s |\n", "5", "Take a Quiz");   
        System.out.println("-------------------------------------------------|");
        System.out.printf("| %-3s | %-40s |\n", "6", "Exit");
        System.out.println(repeatString("-", 50));

        int choice = readIntegerInput("Choose an option: ");

        if (choice < 1 || choice > 6) {
            System.out.println("Invalid option. Please choose a number between 1 and 7.");
            continue;
        }

        switch (choice) {
            case 1:
                quizMenu();
                break;
            case 2:
                questionMenu();
                break;
            case 3:
                userMenu();
                
                break;
            case 4:
                reportMenu();
                break;
            case 5:
                takeQuiz();
                break;
            
            case 6:
                System.out.println("Exiting...");
                closeConnection(); 
                return;
        }
    }
}



    private void quizMenu() {
    while (true) {
        System.out.println(repeatString("-", 50));
        System.out.println("|                Quiz Menu                      |");
        System.out.println(repeatString("-", 50));
        System.out.printf("| %-3s | %-40s |\n", "1", "Add Quiz");
        System.out.printf("| %-3s | %-40s |\n", "2", "View Quizzes");
        System.out.printf("| %-3s | %-40s |\n", "3", "Update Quiz");
        System.out.printf("| %-3s | %-40s |\n", "4", "Delete Quiz");
        System.out.printf("| %-3s | %-40s |\n", "5", "Back to Main Menu");
        System.out.println(repeatString("-", 50));

        int choice = readIntegerInput("Choose an option: "); 

        switch (choice) {
            case 1:
                addQuiz();
                break;
            case 2:
                viewQuizzes();
                break;
            case 3:
                updateQuiz();
                break;
            case 4:
                deleteQuiz();
                break;
            case 5:
                return; 
            default:
                System.out.println("Invalid option. Please try again."); 
        }
    }
}


    private void addQuiz() {
        System.out.print("Enter quiz title: ");
        String title = scanner.nextLine();
        System.out.print("Enter quiz description: ");
        String description = scanner.nextLine();

        Quiz quiz = new Quiz();
        quiz.setTitle(title);
        quiz.setDescription(description);

        try {
            quizDAO.addQuiz(quiz);
            System.out.println("Quiz added successfully!");
        } catch (SQLException | IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void viewQuizzes() {
        try {
            System.out.println(repeatString("-", 70));
            System.out.printf("| %-5s | %-30s | %-50s |\n", "ID", "Title", "Description");
            System.out.println(repeatString("-", 70));
            for (Quiz quiz : quizDAO.viewQuizzes()) {
                System.out.printf("| %-5d | %-30s | %-50s |\n", 
                    quiz.getQuizId(), 
                    quiz.getTitle(), 
                    quiz.getDescription());
            }
            System.out.println(repeatString("-", 70));
        } catch (SQLException e) {
            System.out.println("Error fetching quizzes: " + e.getMessage());
        }
    }

    private void updateQuiz() {
        System.out.print("Enter quiz ID to update: ");
        int quizId = scanner.nextInt();
        scanner.nextLine(); 

        System.out.print("Enter new quiz title: ");
        String title = scanner.nextLine();
        System.out.print("Enter new quiz description: ");
        String description = scanner.nextLine();

        Quiz quiz = new Quiz();
        quiz.setQuizId(quizId);
        quiz.setTitle(title);
        quiz.setDescription(description);

        try {
            quizDAO.updateQuiz(quiz);
            System.out.println("Quiz updated successfully!");
        } catch (SQLException | IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void deleteQuiz() {
        System.out.print("Enter quiz ID to delete: ");
        int quizId = scanner.nextInt();
        scanner.nextLine(); 

        try {
            quizDAO.deleteQuiz(quizId);
            System.out.println("Quiz deleted successfully!");
        } catch (SQLException | IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

  private void questionMenu() {
    while (true) {
        System.out.println(repeatString("-", 50));
        System.out.println("|                Question Menu                  |");
        System.out.println(repeatString("-", 50));
        System.out.printf("| %-3s | %-40s |\n", "1", "Add Question");
        System.out.printf("| %-3s | %-40s |\n", "2", "View Questions");
        System.out.printf("| %-3s | %-40s |\n", "3", "Update Question");
        System.out.printf("| %-3s | %-40s |\n", "4", "Delete Question");
        System.out.printf("| %-3s | %-40s |\n", "5", "Back to Main Menu");
        System.out.println(repeatString("-", 50));

        int choice = readIntegerInput("Choose an option: "); 

        switch (choice) {
            case 1:
                addQuestion();
                break;
            case 2:
                viewQuestions();
                break;
            case 3:
                updateQuestion();
                break;
            case 4:
                deleteQuestion();
                break;
            case 5:
                return; 
            default:
                System.out.println("Invalid option. Please try again."); 
        }
    }
}



   private void addQuestion() {
    String sql = "INSERT INTO questions (quiz_id, question_text, option_a, option_b, option_c, option_d, correct_option) VALUES (?, ?, ?, ?, ?, ?, ?)";
    try (PreparedStatement stmt = connection.prepareStatement(sql)) {
        System.out.print("Enter quiz ID for the question: ");
        String quizIdInput = scanner.nextLine();  
        if (quizIdInput.trim().isEmpty()) {
            System.out.println("No data provided for quiz ID.");
            return; 
        }
        int quizId = Integer.parseInt(quizIdInput); 

        System.out.print("Enter question text: ");
        String questionText = scanner.nextLine();
        if (questionText.trim().isEmpty()) {
            System.out.println("No data provided for question text.");
            return; 
        }

        String[] options = new String[4];
        char optionLabel = 'A';
        for (int i = 0; i < options.length; i++) {
            System.out.printf("Enter option %c: ", optionLabel++);
            options[i] = scanner.nextLine();
            if (options[i].trim().isEmpty()) {
                System.out.println("No data provided for option " + optionLabel);
                return; 
            }
        }

        System.out.print("Enter correct option (A/B/C/D): ");
        String correctOptionInput = scanner.nextLine();
        if (correctOptionInput.trim().isEmpty() || "ABCD".indexOf(correctOptionInput.toUpperCase()) == -1) {
            System.out.println("No valid correct option provided.");
            return; 
        }

        stmt.setInt(1, quizId);
        stmt.setString(2, questionText);
        stmt.setString(3, options[0]);
        stmt.setString(4, options[1]);
        stmt.setString(5, options[2]);
        stmt.setString(6, options[3]);
        stmt.setString(7, correctOptionInput.toUpperCase());
        stmt.executeUpdate();
        System.out.println("\nQuestion added successfully!");
    } catch (SQLException e) {
        System.out.println("Error: " + e.getMessage());
    } catch (NumberFormatException e) {
        System.out.println("Invalid number format for quiz ID.");
    }
}
private void viewQuestions() {
        String sql = "SELECT * FROM questions WHERE quiz_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            System.out.print("Enter quiz ID to view questions: ");
            int quizId = scanner.nextInt();
            scanner.nextLine(); 

            stmt.setInt(1, quizId);
            try (ResultSet rs = stmt.executeQuery()) {
                System.out.println("\n" + repeatString("-", 70));
                System.out.printf("| %-5s | %-50s |\n", "ID", "Question Text");
                System.out.println(repeatString("-", 70));
                while (rs.next()) {
                    System.out.printf("| %-5d | %-50s |\n", 
                        rs.getInt("question_id"), 
                        rs.getString("question_text"));
                }
                System.out.println(repeatString("-", 70));
            }
        } catch (SQLException e) {
            System.out.println("Error fetching questions: " + e.getMessage());
        }
    }
private void updateQuestion() {
    String sql = "UPDATE questions SET question_text = ?, option_a = ?, option_b = ?, option_c = ?, option_d = ?, correct_option = ? WHERE question_id = ?";
    try (PreparedStatement stmt = connection.prepareStatement(sql)) {
        System.out.print("Enter question ID to update: ");
        String questionIdInput = scanner.nextLine();  
        if (questionIdInput.trim().isEmpty()) {
            System.out.println("No data provided for question ID.");
            return; 
        }
        int questionId = Integer.parseInt(questionIdInput); 

        System.out.print("Enter new question text: ");
        String questionText = scanner.nextLine();
        if (questionText.trim().isEmpty()) {
            System.out.println("No data provided for question text.");
            return; 
        }
        
        System.out.print("Enter new option A: ");
        String optionA = scanner.nextLine();
        if (optionA.trim().isEmpty()) {
            System.out.println("No data provided for option A.");
            return; 
        }

        System.out.print("Enter new option B: ");
        String optionB = scanner.nextLine();
        if (optionB.trim().isEmpty()) {
            System.out.println("No data provided for option B.");
            return; 
        }

        System.out.print("Enter new option C: ");
        String optionC = scanner.nextLine();
        if (optionC.trim().isEmpty()) {
            System.out.println("No data provided for option C.");
            return; 
        }

        System.out.print("Enter new option D: ");
        String optionD = scanner.nextLine();
        if (optionD.trim().isEmpty()) {
            System.out.println("No data provided for option D.");
            return; 
        }

        System.out.print("Enter new correct option (A/B/C/D): ");
        String correctOptionInput = scanner.nextLine();
        if (correctOptionInput.trim().isEmpty() || "ABCD".indexOf(correctOptionInput.toUpperCase()) == -1) {
            System.out.println("No valid correct option provided.");
            return; 
        }

        stmt.setString(1, questionText);
        stmt.setString(2, optionA);
        stmt.setString(3, optionB);
        stmt.setString(4, optionC);
        stmt.setString(5, optionD);
        stmt.setString(6, correctOptionInput.toUpperCase());
        stmt.setInt(7, questionId);
        stmt.executeUpdate();
        System.out.println("\nQuestion updated successfully!");
    } catch (SQLException e) {
        System.out.println("Error: " + e.getMessage());
    } catch (NumberFormatException e) {
        System.out.println("Invalid number format for question ID.");
    }
}

private void deleteQuestion() {
    String sql = "DELETE FROM questions WHERE question_id = ?";
    try (PreparedStatement stmt = connection.prepareStatement(sql)) {
        System.out.print("Enter question ID to delete: ");
        String questionIdInput = scanner.nextLine();  
        if (questionIdInput.trim().isEmpty()) {
            System.out.println("No data provided for question ID.");
            return; 
        }
        int questionId = Integer.parseInt(questionIdInput); 

        stmt.setInt(1, questionId);
        stmt.executeUpdate();
        System.out.println("\nQuestion deleted successfully!");
    } catch (SQLException e) {
        System.out.println("Error: " + e.getMessage());
    } catch (NumberFormatException e) {
        System.out.println("Invalid number format for question ID.");
    }
}private void userMenu() {
    while (true) {
        System.out.println(repeatString("-", 50));
        System.out.println("|                User Menu                     |");
        System.out.println(repeatString("-", 50));
        System.out.printf("| %-3s | %-40s |\n", "1", "Add User");
        System.out.printf("| %-3s | %-40s |\n", "2", "View Users");
        System.out.printf("| %-3s | %-40s |\n", "3", "Update User");
        System.out.printf("| %-3s | %-40s |\n", "4", "Delete User");
        System.out.printf("| %-3s | %-40s |\n", "5", "Back to Main Menu");
        System.out.println(repeatString("-", 50));

        int choice = readIntegerInput("Choose an option: ");

        switch (choice) {
            case 1:
                addUser();
                break;
            case 2:
                viewUsers();
                break;
            case 3:
                updateUser();
                break;
            case 4:
                deleteUser();
                break;
            case 5:
                return;
            default:
                System.out.println("Invalid option. Please try again.");
        }
    }
}


private void addUser() {
    
    int userId = readIntegerInput("Enter a valid user ID: "); 
    scanner.next(); 

    System.out.print("Enter username: ");
    String username = scanner.nextLine();
    System.out.print("Enter email: ");
    String email = scanner.nextLine();

    User user = new User();
    user.setUserId(userId); 
    user.setUsername(username);
    user.setEmail(email);

    try {
        userDAO.addUser(user);
        System.out.println("User added successfully!");
    } catch (SQLException e) {
        System.out.println("Error: " + e.getMessage());
    }
}



    
    private void viewUsers() {
        try {
            System.out.println(repeatString("-", 70));
            System.out.printf("| %-5s | %-30s | %-30s |\n", "ID", "Username", "Email");
            System.out.println(repeatString("-", 70));
            for (User user : userDAO.viewUsers()) {
                System.out.printf("| %-5d | %-30s | %-30s |\n", 
                    user.getUserId(), 
                    user.getUsername(), 
                    user.getEmail());
            }
            System.out.println(repeatString("-", 70));
        } catch (SQLException e) {
            System.out.println("Error fetching users: " + e.getMessage());
        }
    }


private void updateUser() {
    System.out.print("Enter user ID to update: ");
    int userId = scanner.nextInt();
    scanner.nextLine(); 

    System.out.print("Enter new username: ");
    String username = scanner.nextLine();
    System.out.print("Enter new email: ");
    String email = scanner.nextLine();

    User user = new User();
    user.setUserId(userId);
    user.setUsername(username);
    user.setEmail(email);

    try {
        userDAO.updateUser(user);
        System.out.println("User updated successfully!");
    } catch (SQLException e) {
        System.out.println("Error: " + e.getMessage());
    }
}


private void deleteUser() {
    System.out.print("Enter user ID to delete: ");
    int userId = scanner.nextInt();
    scanner.nextLine(); 

    try {
        userDAO.deleteUser(userId);
        System.out.println("User deleted successfully!");
    } catch (SQLException e) {
        System.out.println("Error: " + e.getMessage());
    }
}

    private void reportMenu() {
    while (true) {
        System.out.println(repeatString("-", 50));
        System.out.println("|                Report Menu                   |");
        System.out.println(repeatString("-", 50));
        System.out.printf("| %-3s | %-40s |\n", "1", "View All Quiz Reports");
        System.out.printf("| %-3s | %-40s |\n", "2", "View Individual Quiz Report");
        System.out.printf("| %-3s | %-40s |\n", "3", "View User Scores");
        System.out.printf("| %-3s | %-40s |\n", "4", "Back to Main Menu");
        System.out.println(repeatString("-", 50));

        
        int choice = readIntegerInput("Choose an option: ");

        switch (choice) {
            case 1:
                viewAllReports();
                break;
            case 2:
                viewIndividualReport();
                break;          
            case 3:
                viewUsers();
                reportScores();
                break;
            case 4:
                
                return; 
            default:
                System.out.println("Invalid option. Please try again.");
        }
    }
}



  
private void viewAllReports() {
    
    int filterQuizId = readIntegerInput("Enter quiz ID to filter reports (0 for all): ");
    
    String sql;
    if (filterQuizId == 0) {
      
        sql = "SELECT q.quiz_id, q.title, COUNT(que.question_id) AS number_of_questions " +
              "FROM quizzes q LEFT JOIN questions que ON q.quiz_id = que.quiz_id " +
              "GROUP BY q.quiz_id";
    } else {
        
        sql = "SELECT q.quiz_id, q.title, COUNT(que.question_id) AS number_of_questions " +
              "FROM quizzes q LEFT JOIN questions que ON q.quiz_id = que.quiz_id " +
              "WHERE q.quiz_id = ? " +
              "GROUP BY q.quiz_id";
    }

    try (PreparedStatement stmt = connection.prepareStatement(sql)) {
        if (filterQuizId != 0) {
            stmt.setInt(1, filterQuizId);
        }
        
        try (ResultSet rs = stmt.executeQuery()) {
            System.out.println("\n" + repeatString("-", 70));
            System.out.printf("| %-8s | %-30s | %-25s |\n", "Quiz ID", "Title", "Number of Questions");
            System.out.println(repeatString("-", 70));
            while (rs.next()) {
                System.out.printf("| %-8d | %-30s | %-25d |\n", 
                    rs.getInt("quiz_id"), 
                    rs.getString("title"), 
                    rs.getInt("number_of_questions"));
            }
            System.out.println(repeatString("-", 70));
        }
    } catch (SQLException e) {
        System.out.println("Error fetching reports: " + e.getMessage());
    }
}


private int readIntegerInput(String prompt) {
    while (true) {
        System.out.print(prompt);
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a valid integer.");
        }
    }
}


private void viewIndividualReport() {
    int quizId = readIntegerInput("Enter quiz ID to view the report: "); 

    
    String sql = "SELECT question_id, question_text, option_a, option_b, option_c, option_d, correct_option " +
                 "FROM questions WHERE quiz_id = ?";
    try (PreparedStatement stmt = connection.prepareStatement(sql)) {
        stmt.setInt(1, quizId);
        try (ResultSet rs = stmt.executeQuery()) {
            System.out.println("\n" + repeatString("-", 70));
            System.out.printf("| %-8s | %-50s |\n", "Question ID", "Question Text");
            System.out.println(repeatString("-", 70));
            while (rs.next()) {
                int questionId = rs.getInt("question_id");
                String questionText = rs.getString("question_text");
                String optionA = rs.getString("option_a");
                String optionB = rs.getString("option_b");
                String optionC = rs.getString("option_c");
                String optionD = rs.getString("option_d");
                String correctOption = rs.getString("correct_option"); 

                
                System.out.printf("| %-8d | %-50s |\n", questionId, questionText);
                System.out.println(repeatString("-", 70));

               
                System.out.println("Options:");
                System.out.println("A. " + optionA);
                System.out.println("B. " + optionB);
                System.out.println("C. " + optionC);
                System.out.println("D. " + optionD);
                System.out.println(repeatString("-", 70));

                
                System.out.println("Correct Answer: " + correctOption);
                System.out.println(repeatString("-", 70));
            }
        }
    } catch (SQLException e) {
        System.out.println("Error fetching report for quiz ID " + quizId + ": " + e.getMessage());
    }
}






private int getIntInput(String prompt) {
    while (true) {
        System.out.print(prompt);
        if (scanner.hasNextInt()) {
            int value = scanner.nextInt();
            scanner.nextLine(); 
            return value;
        } else {
            System.out.println("Invalid input. Please enter an integer.");
            scanner.nextLine(); 
        }
    }
}
private void takeQuiz() {
    System.out.print("Enter your user ID: ");
    int userId = scanner.nextInt();
    scanner.nextLine(); 

    System.out.print("Enter quiz ID to take: ");
    int quizId = scanner.nextInt();
    scanner.nextLine(); 

    
    try {
        List<Question> questions = quizDAO.getQuestionsForQuiz(quizId);
        if (questions.isEmpty()) {
            System.out.println("No questions found for this quiz.");
            return;
        }

        int score = 0;
        for (Question question : questions) {
            System.out.println("Question: " + question.getQuestionText());
            System.out.println("A) " + question.getOptionA());
            System.out.println("B) " + question.getOptionB());
            System.out.println("C) " + question.getOptionC());
            System.out.println("D) " + question.getOptionD());
            System.out.print("Your answer (A/B/C/D): ");
            String answer = scanner.nextLine().trim().toUpperCase();

            
            if (answer.equals(question.getCorrectOption())) {
                score++;
            }
        }

        
        quizDAO.recordQuizResult(userId, quizId, score);
        System.out.println("Quiz completed! Your score: " + score + "/" + questions.size());
    } catch (SQLException e) {
        System.out.println("Error taking quiz: " + e.getMessage());
    }
}

private void reportScores() {
    int userId = -1;
    boolean validInput = false;

    while (!validInput) {
        System.out.print("Enter user ID to view quiz scores: ");
        String input = scanner.nextLine(); 

        try {
            userId = Integer.parseInt(input); 
            validInput = true; 
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a valid integer for the user ID."); 
        }
    }

    String sql = "SELECT qr.quiz_id, q.title, qr.score FROM quiz_results qr JOIN quizzes q ON qr.quiz_id = q.quiz_id WHERE qr.user_id = ?";
    try (PreparedStatement stmt = connection.prepareStatement(sql)) {
        stmt.setInt(1, userId);
        try (ResultSet rs = stmt.executeQuery()) {
            System.out.println("\n" + repeatString("-", 70));
            System.out.printf("| %-10s | %-30s | %-10s |\n", "Quiz ID", "Quiz Title", "Score");
            System.out.println(repeatString("-", 70));
            while (rs.next()) {
                System.out.printf("| %-10d | %-30s | %-10d |\n", 
                    rs.getInt("quiz_id"), 
                    rs.getString("title"), 
                    rs.getInt("score"));
            }
            System.out.println(repeatString("-", 70));
        }
    } catch (SQLException e) {
        System.out.println("Error fetching quiz scores: " + e.getMessage());
    }
}






private void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                System.out.println("Error closing the connection: " + e.getMessage());
            }
        }
    }

   

    private String repeatString(String str, int times) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < times; i++) {
            sb.append(str);
        }
        return sb.toString();
    }
}