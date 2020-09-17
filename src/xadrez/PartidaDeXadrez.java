package xadrez;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import pecas.xadrez.Bispo;
import pecas.xadrez.Cavalo;
import pecas.xadrez.Peao;
import pecas.xadrez.Rainha;
import pecas.xadrez.Rei;
import pecas.xadrez.Torre;
import tabuleiro.Peca;
import tabuleiro.Posicao;
import tabuleiro.Tabuleiro;

public class PartidaDeXadrez {

	private int turno;
	private Cor jogadorAtual;
	private Tabuleiro tabuleiro;
	private static boolean xeque;
	private static boolean xequeMate;
	private PecaDeXadrez vulnerabilidadeDeEnPassant;
	private PecaDeXadrez promocao;
	private List<Peca> pecasNoTabuleiro = new ArrayList<>();
	private List<Peca> pecasCapturadas = new ArrayList<>();

	public PartidaDeXadrez() {
		tabuleiro = new Tabuleiro(8, 8);
		turno = 1;
		jogadorAtual = Cor.Branco;
		configuracaoInicial();
	}

	public int getTurno() {
		return turno;
	}

	public Cor getJogadorAtual() {
		return jogadorAtual;
	}

	public PecaDeXadrez getPromocao() {
		return promocao;
	}

	public static boolean getXeque() {
		return xeque;
	}

	public static boolean getXequeMate() {
		return xequeMate;
	}

	public PecaDeXadrez getVulnerabilidadeDeEnpassant() {
		return vulnerabilidadeDeEnPassant;
	}

	public PecaDeXadrez[][] getPecas() {
		PecaDeXadrez[][] mat = new PecaDeXadrez[tabuleiro.getLinhas()][tabuleiro.getColunas()];
		for (int i = 0; i < tabuleiro.getLinhas(); i++) {
			for (int j = 0; j < tabuleiro.getColunas(); j++) {
				mat[i][j] = (PecaDeXadrez) tabuleiro.peca(i, j);
			}
		}
		return mat;
	}

	public boolean[][] possiveisMovimentos(PosicaoXadrez posicaoDeOrigem) {
		Posicao posicao = posicaoDeOrigem.toPosicao();
		validandoPosicaoOrigem(posicao);

		return tabuleiro.peca(posicao).possiveisMovimentos();
	}

	public PecaDeXadrez movendoPeca(PosicaoXadrez posicaoDeOrigem, PosicaoXadrez posicaoDeDestino) {
		Posicao origem = posicaoDeOrigem.toPosicao();
		Posicao destino = posicaoDeDestino.toPosicao();
		validandoPosicaoOrigem(origem);
		validandoPosicaoDestino(origem, destino);
		Peca pecaCaturada = movimentandoPeca(origem, destino);
		if (testeXeque(jogadorAtual)) {
			desfazerMovimento(origem, destino, pecaCaturada);
			throw new ExecoesDoXadrez("Voce nao pode se colocar em xequemate");
		}

		PecaDeXadrez movimentandoAPeca = (PecaDeXadrez) tabuleiro.peca(destino);
		// #Movimento especial de Promocao

		promocao = null;
		if (movimentandoAPeca instanceof Peao) {
			if ((movimentandoAPeca.getCor() == Cor.Branco && destino.getLinha() == 0)
					|| movimentandoAPeca.getCor() == Cor.Preto && destino.getLinha() == 7) {
				promocao = (PecaDeXadrez) tabuleiro.peca(destino);
				promocao = escolhaPecaPromovida("R");
			}

		}
		xeque = (testeXeque(oponente(jogadorAtual))) ? true : false;
		if (testeXequeMate(oponente(jogadorAtual))) {
			xequeMate = true;
		} else {
			proximoTurno();
		}
		// #Movimento especial en passant
		if (movimentandoAPeca instanceof Peao
				&& (destino.getLinha() == origem.getLinha() - 2 || destino.getLinha() == origem.getLinha() + 2)) {
			vulnerabilidadeDeEnPassant = movimentandoAPeca;

		} else {
			vulnerabilidadeDeEnPassant = null;
		}
		return (PecaDeXadrez) pecaCaturada;
	}

	public PecaDeXadrez escolhaPecaPromovida(String tipo) {
		if (promocao == null) {
			throw new IllegalStateException("Nao a peca para receber promocao");
		}
		if (!tipo.equals("B") && !tipo.equals("C") && !tipo.equals("T") && !tipo.equals("R")) {

			throw new InvalidParameterException("Tipo invalio de promocao");
		}
		Posicao posicao = promocao.getPosicaoXadrez().toPosicao();
		Peca p = tabuleiro.removerPeca(posicao);
		pecasNoTabuleiro.remove(p);

		PecaDeXadrez novaPeca = novaPeca(tipo, promocao.getCor());
		tabuleiro.colocarPeca(novaPeca, posicao);
		pecasNoTabuleiro.add(novaPeca);
		return novaPeca;

	}

	private PecaDeXadrez novaPeca(String tipo, Cor cor) {
		if (tipo.equals("B"))
			return new Bispo(tabuleiro, cor);
		if (tipo.equals("C"))
			return new Cavalo(tabuleiro, cor);
		if (tipo.equals("R"))
			return new Rainha(tabuleiro, cor);
		return new Torre(tabuleiro, cor);

	}

	private Peca movimentandoPeca(Posicao origem, Posicao destino) {
		PecaDeXadrez p = (PecaDeXadrez) tabuleiro.removerPeca(origem);
		p.incrementarQuantidadeMovimento();
		Peca pecaCapturada = tabuleiro.removerPeca(destino);
		tabuleiro.colocarPeca(p, destino);

		if (pecaCapturada != null) {
			pecasNoTabuleiro.remove(pecaCapturada);
			pecasCapturadas.add(pecaCapturada);
		}
		// #Movimento especial de castling do lado do rei para a torre

		if (p instanceof Rei && destino.getColuna() == origem.getColuna() + 2) {
			Posicao origemTorre = new Posicao(origem.getLinha(), origem.getColuna() + 3);
			Posicao destinoTorre = new Posicao(origem.getLinha(), origem.getColuna() + 1);
			PecaDeXadrez torre = (PecaDeXadrez) tabuleiro.removerPeca(origemTorre);
			tabuleiro.colocarPeca(torre, destinoTorre);
			torre.incrementarQuantidadeMovimento();
		}
		// #Movimento especial de castlin do lado da rainha para a torre
		if (p instanceof Rei && destino.getColuna() == origem.getColuna() - 2) {
			Posicao origemTorre = new Posicao(origem.getLinha(), origem.getColuna() - 4);
			Posicao destinoTorre = new Posicao(origem.getLinha(), origem.getColuna() - 1);
			PecaDeXadrez torre = (PecaDeXadrez) tabuleiro.removerPeca(origemTorre);
			tabuleiro.colocarPeca(torre, destinoTorre);
			torre.incrementarQuantidadeMovimento();
		}
		// #movimento especial em passant
		if (p instanceof Peao) {
			if (origem.getColuna() != destino.getColuna() && pecaCapturada == null) {
				Posicao posicaoPeao;
				if (p.getCor() == Cor.Branco) {
					posicaoPeao = new Posicao(destino.getLinha() + 1, destino.getColuna());
				} else {
					posicaoPeao = new Posicao(destino.getLinha() - 1, destino.getColuna());
				}
				pecaCapturada = tabuleiro.removerPeca(posicaoPeao);
				pecasCapturadas.add(pecaCapturada);
				pecasNoTabuleiro.remove(pecaCapturada);
			}
		}

		return pecaCapturada;
	}

	private void desfazerMovimento(Posicao origem, Posicao destino, Peca pecaCapturada) {
		PecaDeXadrez p = (PecaDeXadrez) tabuleiro.removerPeca(destino);
		p.decrementarQuantidadeMovimento();
		tabuleiro.colocarPeca(p, origem);

		if (pecaCapturada != null) {
			tabuleiro.colocarPeca(pecaCapturada, destino);
			pecasCapturadas.remove(pecaCapturada);
			pecasNoTabuleiro.add(pecaCapturada);

		}
		// #Movimento especial de castling do lado do rei para a torre

		if (p instanceof Rei && destino.getColuna() == origem.getColuna() + 2) {
			Posicao origemTorre = new Posicao(origem.getLinha(), origem.getColuna() + 3);
			Posicao destinoTorre = new Posicao(origem.getLinha(), origem.getColuna() + 1);
			PecaDeXadrez torre = (PecaDeXadrez) tabuleiro.removerPeca(destinoTorre);
			tabuleiro.colocarPeca(torre, origemTorre);
			torre.decrementarQuantidadeMovimento();
		}
		// #Movimento especial de castlin do lado da rainha para a torre
		if (p instanceof Rei && destino.getColuna() == origem.getColuna() - 2) {
			Posicao origemTorre = new Posicao(origem.getLinha(), origem.getColuna() - 4);
			Posicao destinoTorre = new Posicao(origem.getLinha(), origem.getColuna() - 1);
			PecaDeXadrez torre = (PecaDeXadrez) tabuleiro.removerPeca(destinoTorre);
			tabuleiro.colocarPeca(torre, origemTorre);
			torre.decrementarQuantidadeMovimento();
		}

		// #movimento especial em passant
		if (p instanceof Peao) {
			if (origem.getColuna() != destino.getColuna() && pecaCapturada == vulnerabilidadeDeEnPassant) {
				PecaDeXadrez peao = (PecaDeXadrez) tabuleiro.removerPeca(destino);

				Posicao posicaoPeao;
				if (p.getCor() == Cor.Branco) {
					posicaoPeao = new Posicao(3, destino.getColuna());
				} else {
					posicaoPeao = new Posicao(4, destino.getColuna());
				}
				tabuleiro.colocarPeca(peao, posicaoPeao);

			}
		}

	}

	private void validandoPosicaoOrigem(Posicao posicao) {
		if (!tabuleiro.temPecaNaPosicao(posicao)) {

			throw new ExecoesDoXadrez("Nao Existe peca na posicao de origem.");
		}
		if (jogadorAtual != ((PecaDeXadrez) tabuleiro.peca(posicao)).getCor()) {
			throw new ExecoesDoXadrez("A peca escolhida nao e sua");
		}
		if (!tabuleiro.peca(posicao).existePosicaoParaMover()) {
			throw new ExecoesDoXadrez("Nao e possivel mover para essa posicao");
		}
	}

	private void validandoPosicaoDestino(Posicao origem, Posicao destino) {
		if (!tabuleiro.peca(origem).movimentoPossivel(destino)) {
			throw new ExecoesDoXadrez("A peca escolhida nao pode se mover para a posicao de destino.");
		}
	}

	private void proximoTurno() {
		turno++;
		jogadorAtual = (jogadorAtual == Cor.Branco) ? Cor.Preto : Cor.Branco;
	}

	private Cor oponente(Cor cor) {
		return (cor == Cor.Branco) ? Cor.Preto : Cor.Branco;
	}

	private PecaDeXadrez rei(Cor cor) {
		List<Peca> list = pecasNoTabuleiro.stream().filter(x -> ((PecaDeXadrez) x).getCor() == cor)
				.collect(Collectors.toList());
		for (Peca p : list) {
			if (p instanceof Rei) {
				return (PecaDeXadrez) p;
			}
		}
		throw new IllegalStateException("Não existe o rei " + cor + " no tabuleiro ");
	}

	private boolean testeXeque(Cor cor) {
		Posicao posicaoRei = rei(cor).getPosicaoXadrez().toPosicao();
		List<Peca> pecaAdversario = pecasNoTabuleiro.stream().filter(x -> ((PecaDeXadrez) x).getCor() == oponente(cor))
				.collect(Collectors.toList());
		for (Peca p : pecaAdversario) {
			boolean[][] mat = p.possiveisMovimentos();
			if (mat[posicaoRei.getLinha()][posicaoRei.getColuna()]) {
				return true;
			}
		}

		return false;
	}

	private boolean testeXequeMate(Cor cor) {
		if (!testeXeque(cor)) {
			return false;
		}
		List<Peca> list = pecasNoTabuleiro.stream().filter(x -> ((PecaDeXadrez) x).getCor() == cor)
				.collect(Collectors.toList());
		for (Peca p : list) {
			boolean[][] mat = p.possiveisMovimentos();
			for (int i = 0; i < tabuleiro.getLinhas(); i++) {
				for (int j = 0; j < tabuleiro.getColunas(); j++) {
					if (mat[i][j]) {
						Posicao origem = ((PecaDeXadrez) p).getPosicaoXadrez().toPosicao();
						Posicao destino = new Posicao(i, j);
						Peca pecaCapturada = movimentandoPeca(origem, destino);
						boolean testarXeque = testeXeque(cor);
						desfazerMovimento(origem, destino, pecaCapturada);
						if (!testarXeque) {
							return false;
						}
					}
				}
			}
		}
		return true;
	}

	private void colocarNovaPeca(char coluna, int linha, PecaDeXadrez peca) {
		tabuleiro.colocarPeca(peca, new PosicaoXadrez(coluna, linha).toPosicao());
		pecasNoTabuleiro.add(peca);
	}

	private void configuracaoInicial() {
		colocarNovaPeca('a', 1, new Torre(tabuleiro, Cor.Branco));
		colocarNovaPeca('b', 1, new Cavalo(tabuleiro, Cor.Branco));
		colocarNovaPeca('c', 1, new Bispo(tabuleiro, Cor.Branco));
		colocarNovaPeca('d', 1, new Rainha(tabuleiro, Cor.Branco));
		colocarNovaPeca('e', 1, new Rei(tabuleiro, Cor.Branco, this));
		colocarNovaPeca('f', 1, new Bispo(tabuleiro, Cor.Branco));
		colocarNovaPeca('g', 1, new Cavalo(tabuleiro, Cor.Branco));
		colocarNovaPeca('h', 1, new Torre(tabuleiro, Cor.Branco));
		colocarNovaPeca('a', 2, new Peao(tabuleiro, Cor.Branco, this));
		colocarNovaPeca('b', 2, new Peao(tabuleiro, Cor.Branco, this));
		colocarNovaPeca('c', 2, new Peao(tabuleiro, Cor.Branco, this));
		colocarNovaPeca('d', 2, new Peao(tabuleiro, Cor.Branco, this));
		colocarNovaPeca('e', 2, new Peao(tabuleiro, Cor.Branco, this));
		colocarNovaPeca('f', 2, new Peao(tabuleiro, Cor.Branco, this));
		colocarNovaPeca('g', 2, new Peao(tabuleiro, Cor.Branco, this));
		colocarNovaPeca('h', 2, new Peao(tabuleiro, Cor.Branco, this));

		colocarNovaPeca('a', 8, new Torre(tabuleiro, Cor.Preto));
		colocarNovaPeca('b', 8, new Cavalo(tabuleiro, Cor.Preto));
		colocarNovaPeca('c', 8, new Bispo(tabuleiro, Cor.Preto));
		colocarNovaPeca('d', 8, new Rainha(tabuleiro, Cor.Preto));
		colocarNovaPeca('e', 8, new Rei(tabuleiro, Cor.Preto, this));
		colocarNovaPeca('f', 8, new Bispo(tabuleiro, Cor.Preto));
		colocarNovaPeca('g', 8, new Cavalo(tabuleiro, Cor.Preto));
		colocarNovaPeca('h', 8, new Torre(tabuleiro, Cor.Preto));
		colocarNovaPeca('a', 7, new Peao(tabuleiro, Cor.Preto, this));
		colocarNovaPeca('b', 7, new Peao(tabuleiro, Cor.Preto, this));
		colocarNovaPeca('c', 7, new Peao(tabuleiro, Cor.Preto, this));
		colocarNovaPeca('d', 7, new Peao(tabuleiro, Cor.Preto, this));
		colocarNovaPeca('e', 7, new Peao(tabuleiro, Cor.Preto, this));
		colocarNovaPeca('f', 7, new Peao(tabuleiro, Cor.Preto, this));
		colocarNovaPeca('g', 7, new Peao(tabuleiro, Cor.Preto, this));
		colocarNovaPeca('h', 7, new Peao(tabuleiro, Cor.Preto, this));
	}
}
