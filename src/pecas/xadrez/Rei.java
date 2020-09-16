package pecas.xadrez;

import tabuleiro.Posicao;
import tabuleiro.Tabuleiro;
import xadrez.Cor;
import xadrez.PartidaDeXadrez;
import xadrez.PecaDeXadrez;

public class Rei extends PecaDeXadrez {

	private PartidaDeXadrez partidaXadrez;

	public Rei(Tabuleiro tabuleiro, Cor cor, PartidaDeXadrez partidaXadrez) {
		super(tabuleiro, cor);
		this.partidaXadrez = partidaXadrez;
	}

	@Override
	public String toString() {
		return "K";
	}

	private boolean podeMover(Posicao posicao) {
		PecaDeXadrez p = (PecaDeXadrez) getTabuleiro().peca(posicao);
		return p == null || p.getCor() != getCor();
	}

	public boolean testeCastlingTorre(Posicao posicao) {
		PecaDeXadrez p = (PecaDeXadrez) getTabuleiro().peca(posicao);
		return p != null && p instanceof Torre && p.getCor() == getCor() && p.getQuantidadeMovimento() == 0;

	}

	@Override
	public boolean[][] possiveisMovimentos() {
		boolean[][] mat = new boolean[getTabuleiro().getLinhas()][getTabuleiro().getColunas()];

		Posicao p = new Posicao(0, 0);

		// acima
		p.atualizarPosicao(posicao.getLinha() - 1, posicao.getColuna());
		if (getTabuleiro().posicaoExiste(p) && podeMover(p)) {
			mat[p.getLinha()][p.getColuna()] = true;
		}

		// abaixo
		p.atualizarPosicao(posicao.getLinha() + 1, posicao.getColuna());
		if (getTabuleiro().posicaoExiste(p) && podeMover(p)) {
			mat[p.getLinha()][p.getColuna()] = true;
		}

		// esquerda
		p.atualizarPosicao(posicao.getLinha(), posicao.getColuna() - 1);
		if (getTabuleiro().posicaoExiste(p) && podeMover(p)) {
			mat[p.getLinha()][p.getColuna()] = true;
		}

		// direita
		p.atualizarPosicao(posicao.getLinha(), posicao.getColuna() + 1);
		if (getTabuleiro().posicaoExiste(p) && podeMover(p)) {
			mat[p.getLinha()][p.getColuna()] = true;
		}

		// noroeste
		p.atualizarPosicao(posicao.getLinha() - 1, posicao.getColuna() - 1);
		if (getTabuleiro().posicaoExiste(p) && podeMover(p)) {
			mat[p.getLinha()][p.getColuna()] = true;
		}

		// nordeste
		p.atualizarPosicao(posicao.getLinha() - 1, posicao.getColuna() + 1);
		if (getTabuleiro().posicaoExiste(p) && podeMover(p)) {
			mat[p.getLinha()][p.getColuna()] = true;
		}

		// sudoeste
		p.atualizarPosicao(posicao.getLinha() + 1, posicao.getColuna() - 1);
		if (getTabuleiro().posicaoExiste(p) && podeMover(p)) {
			mat[p.getLinha()][p.getColuna()] = true;
		}

		// sudeste
		p.atualizarPosicao(posicao.getLinha() + 1, posicao.getColuna() + 1);
		if (getTabuleiro().posicaoExiste(p) && podeMover(p)) {
			mat[p.getLinha()][p.getColuna()] = true;
		}

		// #Movimento especial castlin
		if (getQuantidadeMovimento() == 0 && !PartidaDeXadrez.getXeque()) {
			// #movimento especial do castling do rei e da torre
			Posicao posicaoT1 = new Posicao(posicao.getLinha(), posicao.getColuna() + 3);
			if (testeCastlingTorre(posicaoT1)) {
				Posicao p1 = new Posicao(posicao.getLinha(), posicao.getColuna() + 1);
				Posicao p2 = new Posicao(posicao.getLinha(), posicao.getColuna() + 2);
				if (getTabuleiro().peca(p1) == null && getTabuleiro().peca(p2) == null) {
					mat[posicao.getLinha()][posicao.getColuna() + 2] = true;
				}
			}
			if (getQuantidadeMovimento() == 0 && !PartidaDeXadrez.getXeque()) {
				// #movimento especial do castling do rei e da torre
				Posicao posicaoT2 = new Posicao(posicao.getLinha(), posicao.getColuna() - 4);
				if (testeCastlingTorre(posicaoT2)) {
					Posicao p1 = new Posicao(posicao.getLinha(), posicao.getColuna() - 1);
					Posicao p2 = new Posicao(posicao.getLinha(), posicao.getColuna() - 2);
					Posicao p3 = new Posicao(posicao.getLinha(), posicao.getColuna() - 3);

					if (getTabuleiro().peca(p1) == null && getTabuleiro().peca(p2) == null
							&& getTabuleiro().peca(p3) == null) {
						mat[posicao.getLinha()][posicao.getColuna() - 2] = true;
					}
				}
			}

		}

		return mat;

	}

}
