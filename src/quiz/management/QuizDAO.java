
package quiz.management;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class QuizDAO {
    private Connection connection;

    public QuizDAO(Connection connection) {
        this.connection = connection;
    }

   

   public void addQuiz(Quiz quiz) throws SQLException {
    String sql = "INSERT INTO quizzes (title, description) VALUES (?, ?)";
    try (PreparedStatement stmt = connection.prepareStatement(sql)) {
        if (quiz.getTitle() == null || quiz.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("Title cannot be empty");
        }
        if (quiz.getDescription() == null || quiz.getDescription().trim().isEmpty()) {
            throw new IllegalArgumentException("Description cannot be empty");
        }
        stmt.setString(1, quiz.getTitle());
        stmt.setString(2, quiz.getDescription());
        stmt.executeUpdate();
    }
}

    public List<Quiz> viewQuizzes() throws SQLException {
        List<Quiz> quizzes = new ArrayList<>();
        String sql = "SELECT * FROM quizzes";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Quiz quiz = new Quiz();
                quiz.setQuizId(rs.getInt("quiz_id"));
                quiz.setTitle(rs.getString("title"));
                quiz.setDescription(rs.getString("description"));
                quizzes.add(quiz);
            }
        }
        return quizzes;
    }

    public void updateQuiz(Quiz quiz) throws SQLException {
    String sql = "UPDATE quizzes SET title = ?, description = ? WHERE quiz_id = ?";
    try (PreparedStatement stmt = connection.prepareStatement(sql)) {
        if (quiz.getQuizId() <= 0) {
            throw new IllegalArgumentException("Invalid quiz ID");
        }
        if (quiz.getTitle() == null || quiz.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("Title cannot be empty");
        }
        stmt.setString(1, quiz.getTitle());
        stmt.setString(2, quiz.getDescription());
        stmt.setInt(3, quiz.getQuizId());
        stmt.executeUpdate();
    }
}

    public void deleteQuiz(int quizId) throws SQLException {
        String sql = "DELETE FROM quizzes WHERE quiz_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            if (quizId <= 0) {
                throw new IllegalArgumentException("Invalid quiz ID");
            }
            stmt.setInt(1, quizId);
            stmt.executeUpdate();
        }
    }
    public void recordQuizResult(int userId, int quizId, int score) throws SQLException {
    String sql = "INSERT INTO quiz_results (user_id, quiz_id, score) VALUES (?, ?, ?)";
    try (PreparedStatement stmt = connection.prepareStatement(sql)) {
        stmt.setInt(1, userId);
        stmt.setInt(2, quizId);
        stmt.setInt(3, score);
        stmt.executeUpdate();
    }
}
public List<Question> getQuestionsForQuiz(int quizId) throws SQLException {
    List<Question> questions = new ArrayList<>();
    String sql = "SELECT * FROM questions WHERE quiz_id = ?";
    
    try (PreparedStatement stmt = connection.prepareStatement(sql)) {
        stmt.setInt(1, quizId);
        try (ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Question question = new Question();
                question.setQuestionId(rs.getInt("question_id"));
                question.setQuizId(rs.getInt("quiz_id"));
                question.setQuestionText(rs.getString("question_text"));
                question.setOptionA(rs.getString("option_a"));
                question.setOptionB(rs.getString("option_b"));
                question.setOptionC(rs.getString("option_c"));
                question.setOptionD(rs.getString("option_d"));
                question.setCorrectOption(rs.getString("correct_option"));
                questions.add(question);
            }
        }
    }
    return questions;
}

}
