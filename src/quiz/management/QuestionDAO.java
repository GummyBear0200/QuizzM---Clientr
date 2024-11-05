
package quiz.management;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class QuestionDAO{
    private Connection connection;

    public QuestionDAO(Connection connection) {
        this.connection = connection;
    }

    public void addQuestion(Question question) throws SQLException {
        String sql = "INSERT INTO questions (quiz_id, question_text, option_a, option_b, option_c, option_d, correct_option) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            if (question.getQuizId() <= 0 || question.getQuestionText() == null || question.getQuestionText().isEmpty() ||
                question.getOptionA() == null || question.getOptionB() == null || question.getOptionC() == null || 
                question.getOptionD() == null || "ABCD".indexOf(question.getCorrectOption()) == -1) {
                throw new IllegalArgumentException("Invalid question data");
            }
            stmt.setInt(1, question.getQuizId());
            stmt.setString(2, question.getQuestionText());
            stmt.setString(3, question.getOptionA());
            stmt.setString(4, question.getOptionB());
            stmt.setString(5, question.getOptionC());
            stmt.setString(6, question.getOptionD());
            stmt.setString(7, String.valueOf(question.getCorrectOption()));
            stmt.executeUpdate();
        }
    }

    public List<Question> viewQuestions(int quizId) throws SQLException {
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
    

    public void updateQuestion(Question question) throws SQLException {
        String sql = "UPDATE questions SET question_text = ?, option_a = ?, option_b = ?, option_c = ?, option_d = ?, correct_option = ? WHERE question_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            if (question.getQuestionId() <= 0 || question.getQuestionText() == null || question.getQuestionText().isEmpty() ||
                question.getOptionA() == null || question.getOptionB() == null || question.getOptionC() == null || 
                question.getOptionD() == null || "ABCD".indexOf(question.getCorrectOption()) == -1) {
                throw new IllegalArgumentException("Invalid question data");
            }
            stmt.setString(1, question.getQuestionText());
            stmt.setString(2, question.getOptionA());
            stmt.setString(3, question.getOptionB());
            stmt.setString(4, question.getOptionC());
            stmt.setString(5, question.getOptionD());
            stmt.setString(6, String.valueOf(question.getCorrectOption()));
            stmt.setInt(7, question.getQuestionId());
            stmt.executeUpdate();
        }
    }

    public void deleteQuestion(int questionId) throws SQLException {
        String sql = "DELETE FROM questions WHERE question_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            if (questionId <= 0) {
                throw new IllegalArgumentException("Invalid question ID");
            }
            stmt.setInt(1, questionId);
            stmt.executeUpdate();
        }
    }
}
