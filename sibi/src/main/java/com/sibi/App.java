package com.sibi;

import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.plaf.LabelUI;
import javax.swing.text.LabelView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.border.Border;

import java.awt.*;
import javax.swing.border.EmptyBorder;

public class App 
{
    static Database db = new Database();
    public static void main( String[] args )
    {
        selectSuggestionMethod();
    }

    private static void selectSuggestionMethod()
   {
      final JFrame frame = new JFrame("RECOMENDIT");

      final JPanel sidebar;
      final JButton button1, button2, button3;
      
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.setSize(500, 200);
      frame.setLocationRelativeTo(null);
      

      final JPanel panel = new JPanel();
      panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
      panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
      panel.setBackground(Color.black);

      JLabel label = new JLabel();

// Crea una nuova istanza di ImageIcon utilizzando il percorso dell'immagine
      ImageIcon icon = new ImageIcon("C:/Users/crist/SIBI-project/sibi/img/logo.png");

      // Imposta l'icona del JLabel come l'immagine
      label.setIcon(icon);

      // Aggiungi il JLabel al JPanel
      panel.add(label);
            

      sidebar = new JPanel();
      sidebar.setBackground(Color.white);
      sidebar.setPreferredSize(new Dimension(150, 0));
      frame.add(sidebar, BorderLayout.WEST);

      JLabel jlabel = new JLabel("you choose the best way!");
      jlabel.setBorder(new EmptyBorder(0,0, 30, 0));
  
      button1 = new JButton("Suggest by Genre");
      button1.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            frame.setVisible(false);
            showCategories();
         }
      });
      panel.add(button1);
      panel.add(Box.createHorizontalGlue());


      button2 = new JButton("Suggest by Film");
      button2.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            frame.setVisible(false);
            showFilms();
         }
      });
      panel.add(button2);
      panel.add(Box.createHorizontalGlue());


      button3 = new JButton("Close");
      button3.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            db.terminate();
         }
      });
      panel.add(button3);

      frame.add(panel);
      frame.setVisible(true);
  
      sidebar.add(jlabel);
      sidebar.add(button1);
      sidebar.add(button2);
      sidebar.add(button3);
   }

   private static void showFilms() {
    final JFrame frame = new JFrame("Film Suggestion");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setSize(400, 400);
    frame.setLocationRelativeTo(null);

    final JPanel panel = new JPanel();
    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
    panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

    JLabel jlabel = new JLabel("choose your favorite movie");
   jlabel.setBorder(new EmptyBorder(0,0, 30, 0));
   jlabel.setFont(new Font("Arial", Font.BOLD, 25));
   panel.add(jlabel);

   JLabel jlabel1 = new JLabel("and go make the popcorn!");
   jlabel1.setBorder(new EmptyBorder(0,0, 30, 0));
   jlabel1.setFont(new Font("Arial", Font.BOLD, 25));
   panel.add(jlabel1);
    
    final List < String > films = collectFilms();
    DefaultListModel < String > filmsListModel = new DefaultListModel < > ();
    for (String film: films) {
       filmsListModel.addElement(film);
    }
    final JList < String > filmsList = new JList < > (filmsListModel);
    JScrollPane scrollPane = new JScrollPane(filmsList);
    panel.add(scrollPane);

    final JTextField searchBar = new JTextField();
    searchBar.setMaximumSize(new Dimension(1300, 25));
    
    panel.add(searchBar);
    searchBar.getDocument().addDocumentListener(new DocumentListener() {
       public void changedUpdate(DocumentEvent e) {
          filterList();
       }
       public void removeUpdate(DocumentEvent e) {
          filterList();
       }
       public void insertUpdate(DocumentEvent e) {
          filterList();
       }

       public void filterList() {
          String filterText = searchBar.getText();
          List<String> filteredFilms = new ArrayList<>();
          for (String film : films) {
             if (film.toLowerCase().contains(filterText.toLowerCase())) {
                filteredFilms.add(film);
             }
          }
          DefaultListModel<String> newFilmsListModel = new DefaultListModel<>();
          for (String film : filteredFilms) {
             newFilmsListModel.addElement(film);
          }
          filmsList.setModel(newFilmsListModel);
          panel.revalidate();
          panel.repaint();
       }
    });

    //Suggestion button
    JButton suggestButton = new JButton("Suggest");
    suggestButton.setBackground(Color.ORANGE);
    suggestButton.addActionListener(new ActionListener() {
       @Override
       public void actionPerformed(ActionEvent e) {
          String selectedFilm = filmsList.getSelectedValue();
          String filmCategory = getFilmCategory(selectedFilm);
          final List < String > filmsRetrieved = requestSuggestion(filmCategory);
          frame.setTitle(filmCategory + " Films");
          panel.removeAll();

          //create a new JList with the updated list of strings
          DefaultListModel < String > newStringListModel = new DefaultListModel < > ();
          for (String film: filmsRetrieved) {
             newStringListModel.addElement(film);
          }
          final JList < String > suggestedFilms = new JList < > (newStringListModel);
          JScrollPane scrollPane = new JScrollPane(suggestedFilms);
          panel.add(scrollPane);

          final JTextField searchBar = new JTextField();
          searchBar.setMaximumSize(new Dimension(1300, 25));

          panel.add(searchBar);
          searchBar.getDocument().addDocumentListener(new DocumentListener() {
             public void changedUpdate(DocumentEvent e) {
                filterList();
             }
             public void removeUpdate(DocumentEvent e) {
                filterList();
             }
             public void insertUpdate(DocumentEvent e) {
                filterList();
             }

             public void filterList() {
                String filterText = searchBar.getText();
                List<String> filteredFilms = new ArrayList<>();
                for (String film : filmsRetrieved) {
                   if (film.toLowerCase().contains(filterText.toLowerCase())) {
                      filteredFilms.add(film);
                   }
                }
                DefaultListModel<String> newFilmsListModel = new DefaultListModel<>();
                for (String book : filteredFilms) {
                   newFilmsListModel.addElement(book);
                }
                suggestedFilms.setModel(newFilmsListModel);
                panel.revalidate();
                panel.repaint();
             }
          });

          JButton seeMoreButton = new JButton("See More");
          seeMoreButton.addActionListener(new ActionListener() {
             @Override
             public void actionPerformed(ActionEvent e) {
                String selectedFilm = filmsList.getSelectedValue();
                String descriptionRetrieved = retrieveDescription(selectedFilm);

                //frame.setVisible(false);
                showFilmDescription(descriptionRetrieved);
             }
          });
          panel.add(seeMoreButton);

          JButton backButton = new JButton("Back");
          backButton.addActionListener(new ActionListener() {
             @Override
             public void actionPerformed(ActionEvent e) {
                
                frame.setVisible(false);
                showFilms();
             }
          });
          panel.add(backButton);

          //refresh the JPanel
          panel.revalidate();
          panel.repaint();
       }
    });
    panel.add(suggestButton);
    panel.add(Box.createHorizontalGlue());

    //Back button
    JButton backButton = new JButton("Back");
    backButton.setBackground(Color.YELLOW);
    backButton.addActionListener(new ActionListener() {
       @Override
       public void actionPerformed(ActionEvent e) {
          frame.setVisible(false);
          selectSuggestionMethod();
       }
    });
    panel.add(backButton);


    frame.add(panel);
    frame.setVisible(true);
}

    //Suggestion selecting a category
    private static void showCategories() {
        final JFrame frame = new JFrame("Film Suggestion");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 400);
        frame.setLocationRelativeTo(null);

        final JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel jlabel = new JLabel("choose your favorite genre!");
         jlabel.setBorder(new EmptyBorder(0,0, 30, 0));
         jlabel.setFont(new Font("Arial", Font.BOLD, 25));
         panel.add(jlabel);
    
        List < String > categories = collectCategories();
        DefaultListModel < String > categoriesListModel = new DefaultListModel < > ();
        for (String category: categories) {
            categoriesListModel.addElement(category);
        }

        final JList < String > categoriesList = new JList < > (categoriesListModel);
        JScrollPane scrollPane = new JScrollPane(categoriesList);
        panel.add(scrollPane);

        //OK
        //OK
        //OK

        //Suggestion button
        JButton suggestButton = new JButton("Suggest");
        suggestButton.setBackground(Color.ORANGE);
        suggestButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            String selectedCategory = categoriesList.getSelectedValue();
            final List < String > films = requestSuggestion(selectedCategory);
            frame.setTitle(selectedCategory + " Films");
            panel.removeAll();

            //create a new JList with the updated list of strings
            DefaultListModel < String > newStringListModel = new DefaultListModel < > ();
            for (String film: films) {
                newStringListModel.addElement(film);
            }
            final JList < String > suggestedFilms = new JList < > (newStringListModel);
            JScrollPane scrollPane = new JScrollPane(suggestedFilms);
            panel.add(scrollPane);

            final JTextField searchBar = new JTextField();
            searchBar.setMaximumSize(new Dimension(1300, 25));
            
            panel.add(searchBar);
            searchBar.getDocument().addDocumentListener(new DocumentListener() {
                public void changedUpdate(DocumentEvent e) {
                    filterList();
                }
                public void removeUpdate(DocumentEvent e) {
                    filterList();
                }
                public void insertUpdate(DocumentEvent e) {
                    filterList();
                }

                public void filterList() {
                    String filterText = searchBar.getText();
                    List<String> filteredFilms = new ArrayList<>();
                    for (String film : films) {
                    if (film.toLowerCase().contains(filterText.toLowerCase())) {
                        filteredFilms.add(film);
                    }
                    }
                    DefaultListModel<String> newFilmsListModel = new DefaultListModel<>();
                    for (String film : filteredFilms) {
                    newFilmsListModel.addElement(film);
                    }
                    suggestedFilms.setModel(newFilmsListModel);
                    panel.revalidate();
                    panel.repaint();
                }
            });

            JButton seeMoreButton = new JButton("See More");
            seeMoreButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String selectedFilm = suggestedFilms.getSelectedValue();
                    String descriptionRetrieved = retrieveDescription(selectedFilm);

                    //frame.setVisible(false);
                    showFilmDescription(descriptionRetrieved);
                }
            });
            panel.add(seeMoreButton);

            JButton backButton = new JButton("Back");
            backButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    frame.setVisible(false);
                    showCategories();
                }
            });
            panel.add(backButton);

            JButton exitButton = new JButton("Exit");
            exitButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    db.terminate();
                }
            });
            panel.add(exitButton);

            //refresh the JPanel
            panel.revalidate();
            panel.repaint();
        }
        });
        panel.add(suggestButton);
        panel.add(Box.createHorizontalGlue());

        //Back button
        JButton backButton = new JButton("Back");
        backButton.setBackground(Color.yellow);
        backButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            frame.setVisible(false);
            selectSuggestionMethod();
        }
        });
        panel.add(backButton);

        frame.add(panel);
        frame.setVisible(true);
    }

    private static void showFilmDescription(String descriptionRetrieved) {
        final JFrame frame = new JFrame("Book Suggestion");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 500);
        frame.setLocationRelativeTo(null);
  
        final JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
  
        JLabel label = new JLabel(descriptionRetrieved);
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append(descriptionRetrieved + "<br>");
        label.setText("<html>" + strBuilder.toString() + "</html>");
        panel.add(label);
  
        JButton backButton = new JButton("Back");
              backButton.addActionListener(new ActionListener() {
                 @Override
                 public void actionPerformed(ActionEvent e) {
                    
                    frame.setVisible(false);
                    //showBooks();
                 }
              });
              panel.add(backButton);
  
              JButton exitButton = new JButton("Exit");
              exitButton.addActionListener(new ActionListener() {
                 @Override
                 public void actionPerformed(ActionEvent e) {
                    db.terminate();
                 }
              });
              panel.add(exitButton);
  
        frame.add(panel);
        frame.setVisible(true);
     }

    private static List < String > collectCategories() {
        return db.retrieveGenres();
     }

     private static List < String > requestSuggestion(String category) {
        return db.suggestFilm(category);
     }

     private static String retrieveDescription(String selectedBook)
    {
      return db.retrieveDescription(selectedBook);
    }

    private static String getFilmCategory(String bookTitle) {
        return db.getFilmCategory(bookTitle);
    }

    private static List < String > collectFilms() {
        return db.retrieveFilms();
    }
}
