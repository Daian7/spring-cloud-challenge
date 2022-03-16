package br.com.caelum.eats.pagamento.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
	
	@HystrixCommand(fallbackMethod = "confirmaPagamentoFallback", threadPoolKey = "confirmaPagamentoThreadPool")
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
	
	@HystrixCommand(threadPoolKey = "getThreadPool")
	public PagamentoDto getById(Long id) {
		return pagamentoRepo.findById(id)
		.map(PagamentoDto::new)
		.orElseThrow(ResourceNotFoundException::new);
	}
	
	@HystrixCommand(threadPoolKey = "getThreadPool")
	public ResponseEntity<List<PagamentoDto>> getAll() {
		return ResponseEntity.ok(pagamentoRepo.findAll()
				.stream()
				.map(PagamentoDto::new)
				.collect(Collectors.toList()));
	}
}
