package BlogAppUsingSwing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;

class BlogPost {
    private String title;
    private String content;

    public BlogPost(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    @Override
    public String toString() {
        return title + "\n" + content + "\n--------------------------------------------------------------------------------------------------\n";
    }
}

public class MyBlogApp {
    private ArrayList<BlogPost> blogPosts;
    private JFrame mainFrame;
    private JTextArea textArea;
    private final String FILE_NAME = "blogposts.txt";

    public MyBlogApp() {
        blogPosts = new ArrayList<>();
        loadBlogPosts(); // Load blog posts from file
        mainFrame = new JFrame("Simple BlogApp Using Swing");
        mainFrame.setSize(800, 800);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        createMainUI();
    }

    private void createMainUI() {
        JPanel panel = new JPanel();
        mainFrame.add(panel);
        panel.setLayout(new GridLayout(3, 1));

        JButton createButton = new JButton("Create a new blog post");
        JButton viewButton = new JButton("View all blog posts");
        JButton exitButton = new JButton("Exit");

        createButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openCreateBlogFrame();
            }
        });

        viewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openViewBlogsFrame();
            }
        });

        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        panel.add(createButton);
        panel.add(viewButton);
        panel.add(exitButton);
    }

    private void openCreateBlogFrame() {
        JFrame createFrame = new JFrame("Create a new blog post");
        createFrame.setSize(800, 800);

        JPanel createPanel = new JPanel(new BorderLayout(10, 10));

        JLabel titleLabel = new JLabel("Title:");
        JTextField titleField = new JTextField(20);

        JLabel contentLabel = new JLabel("Content:");
        JTextArea contentArea = new JTextArea(5, 20);
        contentArea.setLineWrap(true);

        JButton submitButton = new JButton("Submit");

        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String title = titleField.getText();
                String content = contentArea.getText();
                if (!title.isEmpty() && !content.isEmpty()) {
                    BlogPost newBlogPost = new BlogPost(title, content);
                    blogPosts.add(newBlogPost);
                    saveBlogPost(newBlogPost); // Save the new blog post to the file
                    JOptionPane.showMessageDialog(createFrame, "Blog post created!");
                    createFrame.dispose();
                } else {
                    JOptionPane.showMessageDialog(createFrame, "Please enter both title and content.");
                }
            }
        });

        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.add(titleLabel, BorderLayout.WEST);
        titlePanel.add(titleField, BorderLayout.CENTER);

        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.add(contentLabel, BorderLayout.WEST);
        contentPanel.add(contentArea, BorderLayout.CENTER);

        createPanel.add(titlePanel, BorderLayout.NORTH);
        createPanel.add(contentPanel, BorderLayout.CENTER);
        createPanel.add(submitButton, BorderLayout.SOUTH);

        createFrame.add(createPanel);
        createFrame.setVisible(true);
    }

    private void openViewBlogsFrame() {
        JFrame viewFrame = new JFrame("View all blog posts");
        viewFrame.setSize(500, 500);

        JPanel viewPanel = new JPanel(new BorderLayout());

        textArea = new JTextArea(10, 30);
        textArea.setEditable(false);
        textArea.setText("All Blog Posts:\n");
        textArea.setLineWrap(true);

        // Display all blog posts in the text area
        for (BlogPost post : blogPosts) {
            textArea.append(post.toString());
        }

        JScrollPane scrollPane = new JScrollPane(textArea);
        viewPanel.add(scrollPane, BorderLayout.CENTER);

        JButton backButton = new JButton("Back");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewFrame.dispose();
            }
        });

        viewPanel.add(backButton, BorderLayout.SOUTH);

        viewFrame.add(viewPanel);
        viewFrame.setVisible(true);
    }

    private void saveBlogPost(BlogPost post) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME, true))) {
            writer.write(post.getTitle());
            writer.newLine();
            writer.write(post.getContent());
            writer.newLine();
            writer.write("--------------------------------------------------------------------------------------------------");
            writer.newLine();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(mainFrame, "Error saving blog post: " + e.getMessage());
        }
    }

    private void loadBlogPosts() {
        File file = new File(FILE_NAME);
        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String title;
                String content;
                while ((title = reader.readLine()) != null) {
                    if ((content = reader.readLine()) != null) {
                        blogPosts.add(new BlogPost(title, content));
                        reader.readLine(); // Skip the separator line
                    }
                }
            } catch (IOException e) {
                JOptionPane.showMessageDialog(mainFrame, "Error loading blog posts: " + e.getMessage());
            }
        }
    }

    public void run() {
        mainFrame.setVisible(true);
    }

    public static void main(String[] args) {
        MyBlogApp blogApp = new MyBlogApp();
        blogApp.run();
    }
}
