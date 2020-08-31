package xadrez;

import pecas.xadrez.Rei;
import pecas.xadrez.Torre;

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

	private void colocarNovaPeca(char coluna, int linha, PecaDeXadrez peca) {
		tabuleiro.colocarPeca(peca, new PosicaoXadrez(coluna, linha).toPosicao());
	}

	private void configuracaoInicial() {
		colocarNovaPeca('c', 1, new Torre(tabuleiro, Cor.Branco));
		colocarNovaPeca('c', 2, new Torre(tabuleiro, Cor.Branco));
		colocarNovaPeca('d', 2, new Torre(tabuleiro, Cor.Branco));
		colocarNovaPeca('e', 2, new Torre(tabuleiro, Cor.Branco));
		colocarNovaPeca('e', 1, new Torre(tabuleiro, Cor.Branco));
		colocarNovaPeca('d', 1, new Rei(tabuleiro, Cor.Branco));

		colocarNovaPeca('c', 7, new Torre(tabuleiro, Cor.Preto));
		colocarNovaPeca('c', 8, new Torre(tabuleiro, Cor.Preto));
		colocarNovaPeca('d', 7, new Torre(tabuleiro, Cor.Preto));
		colocarNovaPeca('e', 7, new Torre(tabuleiro, Cor.Preto));
		colocarNovaPeca('e', 8, new Torre(tabuleiro, Cor.Preto));
		colocarNovaPeca('d', 8, new Rei(tabuleiro, Cor.Preto));

	}
}
