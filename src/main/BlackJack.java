package main;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

public class BlackJack {
	
	ArrayList<Card> deck;
	Random random = new Random();
	
	// Setup dealer
	Card hiddenCard;
	ArrayList<Card> dealerHand;
	int dealerSum;
	int dealerAceCount;
	
	// Setup player
	ArrayList<Card> playerHand;
	int playerSum;
	int playerAceCount;
	
	// Setup game window
	int boardWidth = 650;
	int boardHeight = 600;
	int cardWidth = 110;
	int cardHeight = 154;
	JFrame frame = new JFrame("Java Black Jack");
	
	JPanel gamePanel = new JPanel() {
		@Override
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			
			if (showTitleScreen) {
				
				g.setColor(new Color(53, 101, 77));
				g.fillRect(0, 0, boardWidth, boardHeight);
				
				g.setFont(new Font("Serif", Font.BOLD, 60));
				g.setColor(Color.YELLOW);
				g.drawString("Java Black Jack", boardWidth / 2 - 210, boardHeight / 2 - 50);
				
				g.setFont(new Font("Serif", Font.PLAIN, 30));
				g.setColor(Color.WHITE);
				g.drawString("PRESS ENTER", boardWidth / 2 - 100, boardHeight / 2 + 50);
				
				buttonPanel.setVisible(false);
				
			} else {
				buttonPanel.setVisible(true);
				try {
					// Reset background
					g.setColor(new Color(53, 101, 77));
					g.fillRect(0, 0, boardWidth, boardHeight);
					
					// Draw hidden card
					Image hiddenCardImg = new ImageIcon(getClass().getResource("/BACK.png")).getImage();
					if (!stayButton.isEnabled()) {
						hiddenCardImg = new ImageIcon(getClass().getResource(hiddenCard.getImagePath())).getImage();
					}
					g.drawImage(hiddenCardImg, 20, 50, cardWidth, cardHeight, null);
					
					// Draw dealer hands
					for (int i = 0; i < dealerHand.size(); i++) {
						Card card = dealerHand.get(i);
						Image cardImg = new ImageIcon(getClass().getResource(card.getImagePath())).getImage();
						g.drawImage(cardImg, cardWidth + 25 + (cardWidth + 5) * i, 50, cardWidth, cardHeight, null);
					}
					
					// Display dealer score
					g.setFont(new Font("Serif", Font.PLAIN, 25));
					g.setColor(Color.WHITE);
					if (!stayButton.isEnabled()) {
						g.drawString("Dealer : " + dealerSum, boardWidth / 2 - 50, 30);
					} else {
						g.drawString("Dealer : ?", boardWidth / 2 - 50, 30);
					}
					
					// Draw player hands
					for (int i = 0; i < playerHand.size(); i++) {
						Card card = playerHand.get(i);
						Image cardImg = new ImageIcon(getClass().getResource(card.getImagePath())).getImage();
						g.drawImage(cardImg, 20 + (cardWidth + 5) * i, 320, cardWidth, cardHeight, null);
					}
					
					// Display player score
					g.drawString("Player : " + reducePlayerAce(), boardWidth / 2 - 50, 320 + cardHeight + 30);
					
					if (!stayButton.isEnabled()) {
						dealerSum = reduceDealerAce();
						playerSum = reducePlayerAce();
						
						String message = "";
						Color messageColor = Color.WHITE;
						
						if (playerSum > 21) {
							message = "You Lose!";
							messageColor = Color.RED;
						}
						else if (dealerSum > 21) {
							message = "You Win!";
							messageColor = Color.CYAN;
						}
						else if (playerSum == dealerSum) {
							message = "Tie!";
							messageColor = Color.YELLOW;
						}
						else if (playerSum > dealerSum) {
							message = "You Win!";
							messageColor = Color.CYAN;
						}
						else if (playerSum < dealerSum) {
							message = "You Lose..";
							messageColor = Color.RED;
						}
						
						g.setFont(new Font("Serif", Font.PLAIN, 50));
						g.setColor(messageColor);
						g.drawString(message, boardWidth / 2 - g.getFontMetrics().stringWidth(message) / 2, boardHeight / 2 - 20);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	};
	
	JPanel buttonPanel = new JPanel();
	JButton hitButton = new JButton("HIT");
	JButton stayButton = new JButton("STAY");
	JButton restartButton = new JButton("RESTART");
	
	boolean showTitleScreen = true;
	
	public BlackJack() {
		
		//startGame();
		frame.setVisible(true);
		frame.setSize(boardWidth, boardHeight);
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		gamePanel.setLayout(new BorderLayout());
		gamePanel.setBackground(new Color(53, 101, 77));
		frame.add(gamePanel);
		
		// Hide buttons in title screen
		hitButton.setVisible(false);
		stayButton.setVisible(false);
		restartButton.setVisible(false);
		
		gamePanel.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				
				if (showTitleScreen && e.getKeyCode() == KeyEvent.VK_ENTER) {
					showTitleScreen = false;
					startGame();
					hitButton.setVisible(true);
					stayButton.setVisible(true); 
					gamePanel.requestFocusInWindow();
					gamePanel.repaint();
					
				}
			}
		});
		gamePanel.setFocusable(true);
		gamePanel.requestFocusInWindow();
		
		// Setup button panel and buttons
		buttonPanel.setBackground(Color.DARK_GRAY);
		
		hitButton.setBackground(Color.DARK_GRAY);
		hitButton.setForeground(Color.WHITE);
		hitButton.setFocusable(false);
		buttonPanel.add(hitButton);
		
		stayButton.setBackground(Color.DARK_GRAY);
		stayButton.setForeground(Color.WHITE);
		stayButton.setFocusable(false);
		buttonPanel.add(stayButton);
		
		frame.add(buttonPanel, BorderLayout.SOUTH);
		
		restartButton.setBackground(Color.DARK_GRAY);
		restartButton.setForeground(Color.WHITE);
		restartButton.setVisible(false);
		
		hitButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				Card card = deck.remove(deck.size() - 1);
				playerSum += card.getValue();
				playerAceCount += card.isAce() ? 1 : 0;
				playerHand.add(card);
				if (reducePlayerAce() > 21) {
					hitButton.setEnabled(false);
				}
				gamePanel.repaint();
			}
		});
		
		stayButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				hitButton.setEnabled(false);
				stayButton.setEnabled(false);
				
				while (dealerSum < 17) {
					Card card = deck.remove(deck.size() - 1);
					dealerSum += card.getValue();
					dealerAceCount += card.isAce() ? 1 : 0;
					dealerHand.add(card);
				}
				gamePanel.repaint();
				
				// Display reset button
				showRestartButton();
			}
		});
		
		restartButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				restartGame();
			}
	    });
		
		gamePanel.repaint();
	}
	
	public void showRestartButton() {
		
		hitButton.setVisible(false);
		stayButton.setVisible(false);
		buttonPanel.removeAll();
		buttonPanel.add(restartButton);
		restartButton.setVisible(true);
		buttonPanel.revalidate();
		buttonPanel.repaint();
	}
	
	public void restartGame() {
		
		startGame();
		hitButton.setEnabled(true);
		stayButton.setEnabled(true);
		restartButton.setVisible(false);
		buttonPanel.removeAll();
		buttonPanel.add(hitButton);
		buttonPanel.add(stayButton);
		hitButton.setVisible(true);
		stayButton.setVisible(true);
		buttonPanel.revalidate();
		buttonPanel.repaint();
		gamePanel.repaint();
	}
	
	public void startGame() {
		
		// Setup deck
		buildDeck();
		shuffleDeck();
		
		// Setup dealer hands
		dealerHand = new ArrayList<Card>();
		dealerSum = 0;
		dealerAceCount = 0;
		
		hiddenCard = deck.remove(deck.size() - 1);
		dealerSum += hiddenCard.getValue();
		dealerAceCount += hiddenCard.isAce() ? 1 : 0;
		
		Card card = deck.remove(deck.size() - 1);
		dealerSum += card.getValue();
		dealerAceCount += card.isAce() ? 1 : 0;
		dealerHand.add(card);
		
		// Setup player hands
		playerHand = new ArrayList<Card>();
		playerSum = 0;
		playerAceCount = 0;
		
		for (int i = 0; i < 2; i++) {
			card = deck.remove(deck.size() - 1);
			playerSum += card.getValue();
			playerAceCount += card.isAce() ? 1 : 0;
			playerHand.add(card);
		}
		
	}
	
	public void buildDeck() {
		
		// Insert all cards in the deck
		deck = new ArrayList<Card>();
		String[] values = {"A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K"};
		String[] types = {"C", "D", "H", "S"};
		for (int i = 0; i < types.length; i++) {
			for (int j = 0; j < values.length; j++) {
				Card card = new Card(values[j], types[i]);
				deck.add(card);
			}
		}
		// Check the deck
		// System.out.println(deck);
	}
	
	public void shuffleDeck() {
		
		for (int i = 0; i < deck.size(); i++) {
			int j = random.nextInt(deck.size());
			Card crrCard = deck.get(i);
			Card randomCard = deck.get(j);
			deck.set(i, randomCard);
			deck.set(j, crrCard);
		}
		// Check the shuffled deck
		// System.out.println(deck);
	}
	
	public int reducePlayerAce() {
		while (playerSum > 21 && playerAceCount > 0) {
			playerSum -= 10;
			playerAceCount -= 1;
		}
		return playerSum;
	}
	
	public int reduceDealerAce() {
		while (dealerSum > 21 && dealerAceCount > 0) {
			dealerSum -= 10;
			dealerAceCount -= 1;
		}
		return dealerSum;
	}

}