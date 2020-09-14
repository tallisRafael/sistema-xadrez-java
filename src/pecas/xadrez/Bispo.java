package pecas.xadrez;

import tabuleiro.Posicao;
import tabuleiro.Tabuleiro;
import xadrez.Cor;
import xadrez.PecaDeXadrez;

public class Bispo extends PecaDeXadrez {

	public Bispo(Tabuleiro tabuleiro, Cor cor) {
		super(tabuleiro, cor);

	}

	@Override
	public String toString() {
		return "B";
	}

	@Override
	public boolean[][] possiveisMovimentos() {
		boolean[][] mat = new boolean[getTabuleiro().getLinhas()][getTabuleiro().getColunas()];

		Posicao p = new Posicao(0, 0);
		// nw

		p.atualizarPosicao(posicao.getLinha() - 1, posicao.getColuna() - 1);
		while (getTabuleiro().posicaoExiste(p) && !getTabuleiro().temPecaNaPosicao(p)) {
			mat[p.getLinha()][p.getColuna()] = true;
			p.atualizarPosicao(p.getLinha() - 1, p.getColuna() - 1);
		}
		if (getTabuleiro().posicaoExiste(p) && existePecaInimiga(p)) {
			mat[p.getLinha()][p.getColuna()] = true;

		}
		// ne
		p.atualizarPosicao(posicao.getLinha() - 1, posicao.getColuna() + 1);
		while (getTabuleiro().posicaoExiste(p) && !getTabuleiro().temPecaNaPosicao(p)) {
			mat[p.getLinha()][p.getColuna()] = true;
			p.atualizarPosicao(p.getLinha() - 1, p.getColuna() + 1);
		}
		if (getTabuleiro().posicaoExiste(p) && existePecaInimiga(p)) {
			mat[p.getLinha()][p.getColuna()] = true;

		}
		// se
		p.atualizarPosicao(posicao.getLinha() + 1, posicao.getColuna() + 1);
		while (getTabuleiro().posicaoExiste(p) && !getTabuleiro().temPecaNaPosicao(p)) {
			mat[p.getLinha()][p.getColuna()] = true;
			p.atualizarPosicao(p.getLinha() + 1, p.getColuna() + 1);
		}
		if (getTabuleiro().posicaoExiste(p) && existePecaInimiga(p)) {
			mat[p.getLinha()][p.getColuna()] = true;

		}
		// sw
		p.atualizarPosicao(posicao.getLinha() + 1, posicao.getColuna() - 1);
		while (getTabuleiro().posicaoExiste(p) && !getTabuleiro().temPecaNaPosicao(p)) {
			mat[p.getLinha()][p.getColuna()] = true;
			p.atualizarPosicao(p.getLinha() + 1, p.getColuna() - 1);
		}
		if (getTabuleiro().posicaoExiste(p) && existePecaInimiga(p)) {
			mat[p.getLinha()][p.getColuna()] = true;

		}

		return mat;

	}
}
