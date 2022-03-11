package br.com.caelum.eats.pagamento.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import br.com.caelum.eats.pagamento.PedidoMudancaDeStatusRequest;

@FeignClient("monolito")
public interface PedidoClient {
	
	@PutMapping("/pedidos/{pedidoId}/status")
	void atualizaStatus(@PathVariable Long pedidoId, @RequestBody PedidoMudancaDeStatusRequest pedidoParaAtualizar);

}
