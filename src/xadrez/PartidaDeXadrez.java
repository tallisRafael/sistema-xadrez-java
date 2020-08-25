package xadrez;

import pecas.xadrez.Rei;
import pecas.xadrez.Torre;
import tabuleiro.Posicao;
import tabuleiro.Tabuleiro;

public class PartidaDeXadrez {
	private Tabuleiro tabuleiro;

	public PartidaDeXadrez() {
		tabuleiro = new Tabuleiro(8, 8);
		configuracaoInicial();

	}

	public PecaDeXadrez[][] getPecas() {
		PecaDeXadrez[][] mat = new PecaDeXadrez[tabuleiro.getColunas()][tabuleiro.getColunas()];
		for (int i = 0; i < tabuleiro.getLinhas(); i++) {
			for (int j = 0; j < tabuleiro.getColunas(); j++) {
				mat[i][j] = (PecaDeXadrez) tabuleiro.peca(i, j);
			}
		}
		return mat;
	}

	private void configuracaoInicial() {
		tabuleiro.colocarPeca(new Torre(tabuleiro, Color.Branco), new Posicao(2, 1));
		tabuleiro.colocarPeca(new Rei(tabuleiro, Color.Preto), new Posicao(0, 4));
		tabuleiro.colocarPeca(new Rei(tabuleiro, Color.Branco), new Posicao(7, 4));
	}
}
