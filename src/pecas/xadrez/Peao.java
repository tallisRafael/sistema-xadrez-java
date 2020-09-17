package pecas.xadrez;

import tabuleiro.Posicao;
import tabuleiro.Tabuleiro;
import xadrez.Cor;
import xadrez.PartidaDeXadrez;
import xadrez.PecaDeXadrez;

public class Peao extends PecaDeXadrez {
	private PartidaDeXadrez partidaDeXadrez;

	public Peao(Tabuleiro tabuleiro, Cor cor, PartidaDeXadrez partidaDeXadrez) {
		super(tabuleiro, cor);
		this.partidaDeXadrez = partidaDeXadrez;

	}

	@Override
	public boolean[][] possiveisMovimentos() {
		boolean[][] mat = new boolean[getTabuleiro().getLinhas()][getTabuleiro().getColunas()];
		Posicao p = new Posicao(0, 0);
		if (getCor() == Cor.Branco) {
			p.atualizarPosicao(posicao.getLinha() - 1, posicao.getColuna());
			if (getTabuleiro().posicaoExiste(p) && !getTabuleiro().temPecaNaPosicao(p)) {
				mat[p.getLinha()][p.getColuna()] = true;
			}
			p.atualizarPosicao(posicao.getLinha() - 2, posicao.getColuna());
			Posicao p2 = new Posicao(posicao.getLinha() - 1, posicao.getColuna());
			if (getTabuleiro().posicaoExiste(p) && !getTabuleiro().temPecaNaPosicao(p)
					&& getTabuleiro().posicaoExiste(p2) && !getTabuleiro().temPecaNaPosicao(p2)
					&& getQuantidadeMovimento() == 0) {
				mat[p.getLinha()][p.getColuna()] = true;
			}
			p.atualizarPosicao(posicao.getLinha() - 1, posicao.getColuna() - 1);
			if (getTabuleiro().posicaoExiste(p) && existePecaInimiga(p)) {
				mat[p.getLinha()][p.getColuna()] = true;
			}
			p.atualizarPosicao(posicao.getLinha() - 1, posicao.getColuna() + 1);
			if (getTabuleiro().posicaoExiste(p) && existePecaInimiga(p)) {
				mat[p.getLinha()][p.getColuna()] = true;

				// #MovimentoEspecial En passant peças brancas
				if (posicao.getLinha() == 3) {
					Posicao esquerda = new Posicao(posicao.getLinha(), posicao.getColuna() - 1);
					if (getTabuleiro().posicaoExiste(esquerda) && existePecaInimiga(esquerda)
							&& getTabuleiro().peca(esquerda) == partidaDeXadrez.getVulnerabilidadeDeEnpassant()) {
						mat[esquerda.getLinha() - 1][esquerda.getColuna()] = true;
					}
					Posicao direita = new Posicao(posicao.getLinha(), posicao.getColuna() + 1);
					if (getTabuleiro().posicaoExiste(direita) && existePecaInimiga(direita)
							&& getTabuleiro().peca(direita) == partidaDeXadrez.getVulnerabilidadeDeEnpassant()) {
						mat[direita.getLinha() - 1][direita.getColuna()] = true;
					}
				}
			}
		} else {
			p.atualizarPosicao(posicao.getLinha() + 1, posicao.getColuna());
			if (getTabuleiro().posicaoExiste(p) && !getTabuleiro().temPecaNaPosicao(p)) {
				mat[p.getLinha()][p.getColuna()] = true;
			}
			p.atualizarPosicao(posicao.getLinha() + 2, posicao.getColuna());
			Posicao p2 = new Posicao(posicao.getLinha() + 1, posicao.getColuna());
			if (getTabuleiro().posicaoExiste(p) && !getTabuleiro().temPecaNaPosicao(p)
					&& getTabuleiro().posicaoExiste(p2) && !getTabuleiro().temPecaNaPosicao(p2)
					&& getQuantidadeMovimento() == 0) {
				mat[p.getLinha()][p.getColuna()] = true;
			}
			p.atualizarPosicao(posicao.getLinha() + 1, posicao.getColuna() - 1);
			if (getTabuleiro().posicaoExiste(p) && existePecaInimiga(p)) {
				mat[p.getLinha()][p.getColuna()] = true;
			}
			p.atualizarPosicao(posicao.getLinha() + 1, posicao.getColuna() + 1);
			if (getTabuleiro().posicaoExiste(p) && existePecaInimiga(p)) {
				mat[p.getLinha()][p.getColuna()] = true;
			}

			if (posicao.getLinha() == 4) {
				Posicao esquerda = new Posicao(posicao.getLinha(), posicao.getColuna() - 1);
				if (getTabuleiro().posicaoExiste(esquerda) && existePecaInimiga(esquerda)
						&& getTabuleiro().peca(esquerda) == partidaDeXadrez.getVulnerabilidadeDeEnpassant()) {
					mat[esquerda.getLinha() + 1][esquerda.getColuna()] = true;
				}
				Posicao direita = new Posicao(posicao.getLinha(), posicao.getColuna() + 1);
				if (getTabuleiro().posicaoExiste(direita) && existePecaInimiga(direita)
						&& getTabuleiro().peca(direita) == partidaDeXadrez.getVulnerabilidadeDeEnpassant()) {
					mat[direita.getLinha() + 1][direita.getColuna()] = true;
				}

			}

		}

		return mat;
	}

	@Override
	public String toString() {
		return "P";
	}

}
