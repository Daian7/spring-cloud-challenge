package br.com.caelum.eats.pagamento.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

import br.com.caelum.eats.pagamento.ClienteRestDoPedido;
import br.com.caelum.eats.pagamento.Pagamento;
import br.com.caelum.eats.pagamento.PagamentoDto;
import br.com.caelum.eats.pagamento.PagamentoRepository;
import br.com.caelum.eats.pagamento.ResourceNotFoundException;

@Service
public class PagamentoService {
	
	@Autowired
	private PagamentoRepository pagamentoRepo;
	
	@Autowired
	private ClienteRestDoPedido pedidoCliente;
	
	@HystrixCommand(fallbackMethod = "confirmaPagamentoFallback")
	public PagamentoDto confirmaPagamento(Long id) {
		Pagamento pagamento = pagamentoRepo.findById(id).orElseThrow(ResourceNotFoundException::new);
		pagamento.setStatus(Pagamento.Status.CONFIRMADO);
		pedidoCliente.notificaPagamentoDoPedido(pagamento.getPedidoId());
		pagamentoRepo.save(pagamento);
		return new PagamentoDto(pagamento);
	}
	
	public PagamentoDto confirmaPagamentoFallback(Long id) {
		Pagamento pagamento = pagamentoRepo.findById(id).orElseThrow(ResourceNotFoundException::new);
		pagamento.setStatus(Pagamento.Status.PROCESSANDO);
		pagamentoRepo.save(pagamento);
		return new PagamentoDto(pagamento);
		
	}

}
